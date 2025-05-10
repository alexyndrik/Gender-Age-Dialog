package com.example.myapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.network.SocketManager
import com.example.myapplication.network.TestRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllowedViewModel @Inject constructor() : ViewModel() {

    private val _allowed = MutableLiveData<Boolean>()
    val allowed: LiveData<Boolean> = _allowed

    fun getAllowed(gender: String, age: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            SocketManager.connect()
            SocketManager.send(TestRequest(gender, age))
            _allowed.postValue(SocketManager.receive().allowed)
            SocketManager.close()
        }
    }

}