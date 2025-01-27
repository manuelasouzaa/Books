package com.example.projeto.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projeto.contextExpresions.UserPreferences
import com.example.projeto.database.Repository
import com.example.projeto.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    suspend fun verifyUserStatus(context: Context): Boolean {
        return getUserStatus(context)
    }

    private suspend fun getUserStatus(context: Context): Boolean {
        return UserPreferences(context).getUserStatus()
    }

    suspend fun getLoggedUser(context: Context): Flow<User>? {
        val userEmail = getUserEmail(context)
        return fetchUser(context, userEmail.toString())
    }

    private fun fetchUser(context: Context, email: String): Flow<User>? {
        return Repository(context).fetchUserByEmail(email)
    }

    suspend fun saveUserInfo(email: String, context: Context) {
        UserPreferences(context).saveUserEmail(email)
    }

    suspend fun getUserEmail(context: Context): String? {
        return UserPreferences(context).getUserEmail()
    }

    suspend fun saveUserStatus(isLoggedIn: Boolean, context: Context) {
        UserPreferences(context).saveUserStatus(isLoggedIn)
    }

    fun removeUser(context: Context) {
        viewModelScope.launch {
            UserPreferences(context).clearUserStatus()
        }
    }
}