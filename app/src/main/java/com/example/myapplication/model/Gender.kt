package com.example.myapplication.model

enum class Gender(val value: String) {
    MALE("m"),
    FEMALE("f");

    companion object {
        fun fromString(value: String?): Gender? {
            return entries.find { it.value == value }
        }
    }
}