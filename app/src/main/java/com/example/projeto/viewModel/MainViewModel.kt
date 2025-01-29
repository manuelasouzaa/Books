package com.example.projeto.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projeto.expresions.UserPreferences
import com.example.projeto.model.User
import com.example.projeto.ui.user.repositories.UserDaoRepository
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
        return UserDaoRepository(context).fetchUserByEmail(email)
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