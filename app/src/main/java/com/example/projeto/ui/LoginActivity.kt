package com.example.projeto.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projeto.R
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.contextExpresions.toast
import com.example.projeto.database.LibraryDatabase
import com.example.projeto.database.Repository
import com.example.projeto.databinding.LoginActivityBinding
import com.example.projeto.model.User
import com.example.projeto.viewModel.MainViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val repository by lazy {
        Repository(this@LoginActivity)
    }
    private val binding by lazy {
        LoginActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        btnEnterConfig()

        binding.linkLoginActivity.setOnClickListener {
            goTo(CadastroActivity::class.java)
        }
    }

    private fun btnEnterConfig() {
        binding.btnEnterLoginActivity.setOnClickListener {
            val email = binding.userEmailLoginActivity.text.toString()
            val password = binding.passwordLoginActivity.text.toString()

            when {
                email == "" || password == "" ->
                    toast("Preencha todos os campos")

                email !== "" && password !== "" -> {
                    lifecycleScope.launch(IO) {
                        verifyIfUserExists(email, password)
                    }
                }
            }
        }
    }

    private suspend fun verifyIfUserExists(email: String, password: String) {
        val user: User? = repository.fetchUserByEmail(email)?.first()
        Log.i("FETCH", "authenticate: ${user}")

        when {
            user == null -> {
                withContext(Main) {
                    toast(getString(R.string.user_not_found))
                }
            }

            else -> {
                verifyPassword(email, password)
            }
        }
    }

    private suspend fun verifyPassword(email: String, password: String) {
        val userConnected = repository.searchUser(email, password)
        if (userConnected == null) {
            withContext(Main) {
                toast("Senha incorreta")
                binding.passwordLoginActivity.text.clear()
            }
        }
        Log.i("SEARCH", "authenticate: ${userConnected?.email + userConnected?.password}")
        if (userConnected !== null)
            authenticate(email)
    }

    private fun authenticate(email: String) {
        lifecycleScope.launch(IO) {
            viewModel.saveUserInfo(email, this@LoginActivity)
            viewModel.saveUserStatus(true, this@LoginActivity)

            withContext(Main) {
                toast(getString(R.string.logged_in))
                goTo(MainActivity::class.java)
            }
        }
    }
}