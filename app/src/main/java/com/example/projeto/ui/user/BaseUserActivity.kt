package com.example.projeto.ui.user

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projeto.expresions.goTo
import com.example.projeto.viewModel.MainViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

abstract class BaseUserActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModels()

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