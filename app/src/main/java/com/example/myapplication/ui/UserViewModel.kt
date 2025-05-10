package com.example.myapplication.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.UserPreferencesRepository
import com.example.myapplication.model.Gender
import com.example.myapplication.model.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repo: UserPreferencesRepository
) : ViewModel() {

    val userPreferences: StateFlow<UserPreferences> = repo.userPreferencesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserPreferences())

    fun updateGender(gender: Gender?) {
        viewModelScope.launch { repo.setGender(gender) }
    }

    fun updateAge(age: Int?) {
        viewModelScope.launch { repo.setAge(age) }
    }
}