package com.example.projeto.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.projeto.contextExpresions.toast
import com.example.projeto.contextExpresions.irPara
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
                criarUsuario()
            }
        }

        binding.link.setOnClickListener {
            irPara(LoginActivity::class.java)
            finish()
        }
    }

    private fun criarUsuario(): User {
        val email = binding.email.text.toString()
        val nome = binding.nomeUsuario.text.toString()
        val senha = binding.senha.text.toString()

        val novoUsuario = User(
            email = email,
            name = nome,
            password = senha
        )

        if (email == "" || senha == "")
            lifecycleScope.launch(Main) {
                toast("Preencha todos os campos")
            }

        dao.salva(novoUsuario)

        lifecycleScope.launch(Main) {
            toast("Cadastro realizado com sucesso!")
            irPara(LoginActivity::class.java) {
                novoUsuario
            }
            finish()

        }
        return novoUsuario
    }
}