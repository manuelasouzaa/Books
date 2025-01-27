package com.example.projeto.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.contextExpresions.toast
import com.example.projeto.database.LibraryDatabase
import com.example.projeto.database.Repository
import com.example.projeto.databinding.CadastroActivityBinding
import com.example.projeto.model.User
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CadastroActivity : AppCompatActivity() {

    private val binding by lazy {
        CadastroActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val btnEnter = binding.btnEnterCadastroActivity

        btnEnter.setOnClickListener {
            signUpNewUser()
        }

        binding.linkCadastroActivity.setOnClickListener {
            goTo(LoginActivity::class.java)
            finish()
        }
    }

    private fun signUpNewUser() {
        val name = binding.usernameCadastroActivity.text.toString()
        val email = binding.userEmailCadastroActivity.text.toString()
        val password = binding.passwordCadastroActivity.text.toString()

        lifecycleScope.launch(IO) {
            verifyData(email, name, password)
        }
    }

    private suspend fun verifyData(email: String, name: String, password: String) {
        when {
            email.isNotEmpty() && name.isNotEmpty() && password.isNotEmpty() -> {
                val user = Repository(this).fetchUserByEmail(email)?.firstOrNull()
                if (user == null)
                    createUser(email, name, password)
                if (user !== null)
                    withContext(Main) {
                        toast("E-mail já cadastrado")
                    }
            }

            email == "" || password == "" || name == "" ->
                lifecycleScope.launch(Main) {
                    toast("Preencha todos os campos")
                }
        }
    }

    private fun createUser(email: String, name: String, password: String): User {
        val newUser = User(
            email = email,
            name = name,
            password = password
        )

        Repository(this@CadastroActivity).saveNewUser(newUser)

        lifecycleScope.launch(Main) {
            toast("Cadastro realizado com sucesso!")
            goTo(LoginActivity::class.java)
            finish()
        }
        return newUser
    }
}