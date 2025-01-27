package com.example.projeto.contextExpresions

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

val Context.userPreferences by preferencesDataStore("preferences")

class UserPreferences(private val context: Context) {

    val USER_INFO_KEY = stringPreferencesKey("user_info")
    val LOGIN_STATUS_KEY = booleanPreferencesKey("is_logged_in")

    suspend fun saveUserEmail(email: String) {
        context.userPreferences.edit { preferences ->
            preferences[USER_INFO_KEY] = email
        }
    }

    suspend fun getUserEmail(): String? {
        val preferences = context.userPreferences.data.first()
        return preferences[USER_INFO_KEY]
    }

    suspend fun saveUserStatus(isLoggedIn: Boolean) {
        context.userPreferences.edit { preferences ->
            preferences[LOGIN_STATUS_KEY] = isLoggedIn
        }
    }

    suspend fun getUserStatus(): Boolean {
        val preferences = context.userPreferences.data.first()
        return preferences[LOGIN_STATUS_KEY] ?: false
    }

    suspend fun clearUserStatus() {
        context.userPreferences.edit { preferences ->
            preferences.remove(USER_INFO_KEY)
            preferences.remove(LOGIN_STATUS_KEY)
        }
    }
}