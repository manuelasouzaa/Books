package com.example.projeto.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.contextExpresions.toast
import com.example.projeto.database.LibraryDatabase
import com.example.projeto.databinding.CadastroActivityBinding
import com.example.projeto.model.User
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class CadastroActivity : AppCompatActivity() {

    private val binding by lazy {
        CadastroActivityBinding.inflate(layoutInflater)
    }

    private val dao by lazy {
        LibraryDatabase.instance(this).userDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val btn = binding.btnEnter

        btn.setOnClickListener {
            lifecycleScope.launch(IO) {
                createUser()
            }
        }

        binding.link.setOnClickListener {
            goTo(LoginActivity::class.java)
            finish()
        }
    }

    private fun createUser(): User {
        val email = binding.userEmail.text.toString()
        val name = binding.userName.text.toString()
        val password = binding.password.text.toString()

        val newUser = User(
            email = email,
            name = name,
            password = password
        )

        if (email == "" || password == "")
            lifecycleScope.launch(Main) {
                toast("Preencha todos os campos")
            }

        dao.save(newUser)

        lifecycleScope.launch(Main) {
            toast("Cadastro realizado com sucesso!")
            goTo(LoginActivity::class.java) {
                newUser
            }
            finish()

        }
        return newUser
    }
}