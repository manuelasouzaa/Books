package com.example.projeto.ui.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projeto.R
import com.example.projeto.ui.user.repositories.UserDaoRepository
import com.example.projeto.expresions.goTo
import com.example.projeto.expresions.toast
import com.example.projeto.databinding.CadastroActivityBinding
import com.example.projeto.model.User
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CadastroActivity : AppCompatActivity() {

    private val binding by lazy {
        CadastroActivityBinding.inflate(layoutInflater)
    }
    private val repository by lazy {
        UserDaoRepository(this@CadastroActivity)
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
                val user = repository.fetchUserByEmail(email)?.firstOrNull()
                if (user == null)
                    createUser(email, name, password)
                if (user !== null)
                    withContext(Main) {
                        toast(getString(R.string.email_cadastrado))
                    }
            }

            email == "" || password == "" || name == "" ->
                lifecycleScope.launch(Main) {
                    toast(getString(R.string.preencha_todos_os_campos))
                }
        }
    }

    private fun createUser(email: String, name: String, password: String): User {
        val newUser = User(
            email = email,
            name = name,
            password = password
        )

        repository.saveNewUser(newUser)

        lifecycleScope.launch(Main) {
            toast(getString(R.string.cadastro_realizado_com_sucesso))
            goTo(LoginActivity::class.java)
            finish()
        }
        return newUser
    }
}