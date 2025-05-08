package com.example.myapplication.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplication.data.model.Gender
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GenderRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val Context.dataStore by preferencesDataStore("user_gender_prefs")
    private val GENDER_KEY = stringPreferencesKey("gender")

    val genderFlow: Flow<Gender?> = context.dataStore.data
        .map { prefs -> Gender.fromString(prefs[GENDER_KEY]) }

    suspend fun saveGender(gender: Gender?) {
        context.dataStore.edit { prefs ->
            if (gender == null) prefs.remove(GENDER_KEY)
            else prefs[GENDER_KEY] = gender.value
        }
    }

}