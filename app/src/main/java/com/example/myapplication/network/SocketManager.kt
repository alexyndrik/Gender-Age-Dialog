package com.example.myapplication.network

import android.util.Log
import com.google.gson.Gson
import java.io.DataInputStream
import java.net.Socket
import java.nio.ByteBuffer

private const val TAG = "SocketManager"

object SocketManager {

    private const val SERVER_ADDRESS = "challenge.ciliz.com"
    private const val SERVER_PORT = 2222

    private var socket: Socket? = null

    fun connect() {
        if (socket == null) socket = Socket(SERVER_ADDRESS, SERVER_PORT)
        Log.d(TAG, "connected: ${socket?.isConnected}")
    }

    fun send(request: TestRequest) {
        val message = Gson().toJson(request) // <- request

        Log.i(TAG, "sending: $message")

        val messageBytes = message.toByteArray()
        val lengthBytes = ByteBuffer.allocate(4).putInt(messageBytes.size).array()

        val outputStream = socket?.getOutputStream()
        outputStream?.write(lengthBytes)
        outputStream?.write(messageBytes)
        outputStream?.flush()
    }

    fun receive(): TestResponse {
        val inputStream = DataInputStream(socket?.getInputStream())

        val lengthBytes = ByteArray(4)
        inputStream.readFully(lengthBytes)
        val length = ByteBuffer.wrap(lengthBytes).int

        val buffer = ByteArray(length)
        inputStream.readFully(buffer)
        val message = String(buffer, 0, length)

        Log.d(TAG, "received: $message")

        return Gson().fromJson(message, TestResponse::class.java) // <- message
    }

    fun close() {
        socket?.close()
        socket = null
    }
}
