package com.example.projeto.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.model.User
import com.example.projeto.viewModel.MainViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

abstract class UserActivity : AppCompatActivity() {

    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    var user: StateFlow<User?> = _user
    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(IO) {

            val usuarioLogado = viewModel.getLoggedUser(this@UserActivity)
            _user.value = usuarioLogado?.first()
        }
    }

    protected fun goToLogin() {
        goTo(LoginActivity::class.java) {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        finish()
    }

    protected fun verifyLoggedUser(context: Context) {
        lifecycleScope.launch(IO) {
            val userStatus = viewModel.verifyUserStatus(context)
            if (!userStatus)
                goToLogin()
        }
    }
}