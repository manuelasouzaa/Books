package com.example.projeto.ui

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.contextExpresions.loggedUser
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

        lifecycleScope.launch {
            user.filterNotNull().collect {
                binding.nomeUsuario.text = user.value?.name
                binding.emailUsuario.text = user.value?.email
            }
        }

        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch(IO) {
                removeUser()
            }
        }

        binding.btnVoltar.setOnClickListener {
            goTo(MainActivity::class.java) {
                loggedUser
            }
        }

        binding.btnFavorites.setOnClickListener {
            goTo(FavoritesActivity::class.java) {
                loggedUser
            }
        }
    }
}