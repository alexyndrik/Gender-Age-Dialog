package com.example.myapplication.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplication.data.model.Age
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AgeRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val Context.dataStore by preferencesDataStore("user_age_prefs")

    private val AGE_KEY = intPreferencesKey("age")

    val ageFlow: Flow<Age?> = context.dataStore.data
        .map { prefs -> prefs[AGE_KEY]?.let { Age(it) } }

    suspend fun saveAge(age: Age?) {
        context.dataStore.edit { prefs ->
            if (age == null) prefs.remove(AGE_KEY)
            else prefs[AGE_KEY] = age.value
        }
    }

}