package com.example.myapplication.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplication.model.Gender
import com.example.myapplication.model.UserPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val Context.dataStore by preferencesDataStore("user_prefs")

    private val GENDER_KEY = stringPreferencesKey("gender")
    private val AGE_KEY = intPreferencesKey("age")

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data
        .map { prefs ->
            val gender = Gender.fromString(prefs[GENDER_KEY])
            val age = prefs[AGE_KEY]
            UserPreferences(gender, age)
        }

    suspend fun setGender(gender: Gender?) {
        context.dataStore.edit {
            if (gender == null) it.remove(GENDER_KEY)
            else it[GENDER_KEY] = gender.value
        }
    }

    suspend fun setAge(age: Int?) {
        context.dataStore.edit {
            if (age == null) it.remove(AGE_KEY)
            else it[AGE_KEY] = age
        }
    }

}