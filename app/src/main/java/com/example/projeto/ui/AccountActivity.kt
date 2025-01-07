package com.example.projeto.ui

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.projeto.contextExpresions.irPara
import com.example.projeto.contextExpresions.usuarioLogado
import com.example.projeto.contextExpresions.usuarioEmail
import com.example.projeto.databinding.AccountActivityBinding
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class AccountActivity : UserActivity() {

    private val binding by lazy {
        AccountActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val emailUsuario = intent.getStringExtra(usuarioEmail)

        lifecycleScope.launch {
            usuario.filterNotNull().collect {
                binding.nomeUsuario.text = usuario.value?.name
                binding.emailUsuario.text = usuario.value?.email
            }
        }

        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch(IO) {
                removerUsuario()
                irParaLogin()
            }
        }

        binding.btnVoltar.setOnClickListener {
            irPara(MainActivity::class.java) {
                usuarioLogado
            }
            finish()
        }

        binding.btnBooklist.setOnClickListener {
            irPara(FavoritesActivity::class.java) {
                putExtra(usuarioEmail, emailUsuario)
                usuarioLogado
            }
        }
    }
}