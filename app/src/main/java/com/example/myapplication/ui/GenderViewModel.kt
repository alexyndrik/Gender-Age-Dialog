package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Gender
import com.example.myapplication.data.repository.GenderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenderViewModel @Inject constructor(
    private val repository: GenderRepository
) : ViewModel() {

    val gender: StateFlow<Gender?> = repository.genderFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun selectGender(gender: Gender?) {
        viewModelScope.launch {
            repository.saveGender(gender)
        }
    }

}