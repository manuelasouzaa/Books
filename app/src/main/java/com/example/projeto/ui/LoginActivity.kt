package com.example.projeto.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.example.projeto.contextExpresions.dataStore
import com.example.projeto.contextExpresions.irPara
import com.example.projeto.contextExpresions.usuarioLogado
import com.example.projeto.contextExpresions.toast
import com.example.projeto.database.LibraryDatabase
import com.example.projeto.databinding.LoginActivityBinding
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
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

        binding.link.setOnClickListener {
            irPara(CadastroActivity::class.java)
            finish()
        }
    }

    private fun btnEnterConfig() {
        binding.btnEnter.setOnClickListener {
            val email = binding.email.text.toString()
            val senha = binding.senha.text.toString()
            autenticar(email, senha)
        }
    }

    private fun autenticar(email: String, password: String) {
        lifecycleScope.launch(IO) {
            dao.buscaUsuario(email, password)?.let { user ->
                launch {
                    dataStore.edit { preferences ->
                        preferences[usuarioLogado] = user.email
                    }
                }
                irPara(MainActivity::class.java) {
                    usuarioLogado
                }
                finish()
            } ?: run {
                withContext(Main) {
                    toast("Usuário não encontrado. Faça o cadastro")
                }
            }
        }
    }
}