package com.example.projeto.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.example.projeto.contextExpresions.dataStore
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.contextExpresions.loggedUser
import com.example.projeto.contextExpresions.toast
import com.example.projeto.database.LibraryDatabase
import com.example.projeto.databinding.LoginActivityBinding
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        LoginActivityBinding.inflate(layoutInflater)
    }

    private val dao by lazy {
        LibraryDatabase.instance(this).userDao()
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
            authenticate(email, password)
        }
    }

    private fun authenticate(email: String, password: String) {
        lifecycleScope.launch(IO) {
            dao.searchUsers(email, password)?.let { user ->
                launch {
                    dataStore.edit { preferences ->
                        preferences[loggedUser] = user.email
                    }

                    withContext(Main) {
                        toast("Login efetuado")
                        goTo(MainActivity::class.java) {
                            loggedUser
                        }
                    }
                }
            } ?: run {
                withContext(Main) {
                    toast("Usuário não encontrado. Faça o cadastro")
                }
            }
        }
    }
}