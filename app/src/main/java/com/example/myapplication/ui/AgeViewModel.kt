package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Age
import com.example.myapplication.data.repository.AgeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgeViewModel @Inject constructor(
    private val repository: AgeRepository
) : ViewModel() {

    val age: StateFlow<Age?> = repository.ageFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun selectAge(age: Age?) {
        viewModelScope.launch {
            repository.saveAge(age)
        }
    }

}