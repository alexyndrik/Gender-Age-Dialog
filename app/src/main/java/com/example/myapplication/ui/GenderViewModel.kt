package com.example.myapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenderViewModel @Inject constructor() : ViewModel() {

    private val _gender = MutableLiveData<String?>()
    val gender: LiveData<String?> = _gender

    fun setGender(gender: String?) {
        viewModelScope.launch {
            _gender.value = gender
        }
    }

}