package com.example.myapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgeViewModel @Inject constructor() : ViewModel() {

    private val _age = MutableLiveData<Int?>()
    val age: LiveData<Int?> = _age

    fun setAge(age: Int?) {
        viewModelScope.launch {
            _age.value = age
        }
    }

}