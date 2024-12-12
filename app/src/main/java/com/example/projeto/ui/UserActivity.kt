package com.example.projeto.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.database.LibraryDatabase
import com.example.projeto.contextExpresions.dataStore
import com.example.projeto.contextExpresions.loggedUser
import com.example.projeto.model.User
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

abstract class UserActivity : AppCompatActivity() {

    private val userDao by lazy {
        LibraryDatabase.instance(this).userDao()
    }

    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    val user: StateFlow<User?> = _user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(IO) {
            launch {
                verifyLoggedUser()
            }
        }
    }

    private suspend fun verifyLoggedUser() {
        dataStore.data.first().let { preferences ->
            preferences[loggedUser]?.let { email ->
                fetchUser(email)
            } ?: goToLogin()
        }
    }

    protected fun goToLogin() {
        goTo(LoginActivity::class.java) {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        finish()
    }

    protected suspend fun fetchUser(email: String): User? {
        return userDao.fetchUserByEmail(email).firstOrNull().also {
            _user.value = it
        }
    }

    protected suspend fun removeUser() {
        dataStore.edit {
            it.remove(loggedUser)
        }
    }
}