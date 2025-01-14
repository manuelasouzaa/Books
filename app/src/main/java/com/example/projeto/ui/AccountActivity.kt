package com.example.projeto.ui

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.contextExpresions.loggedUser
import com.example.projeto.contextExpresions.userEmail
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
                val email = it.email
                val username = it.name
                binding.userName.text = username
                binding.userEmail.text = email
            }
        }

        binding.btnLogout.setOnClickListener {
            lifecycleScope.launch(IO) {
                removeUser()
                goToLogin()
            }
        }

        binding.btnReturn.setOnClickListener {
            goTo(MainActivity::class.java) {
                loggedUser
            }
            finish()
        }

        binding.btnBooklist.setOnClickListener {
            lifecycleScope.launch {
                user.filterNotNull().collect {
                    val email = it.email.toString()
                    goTo(FavoritesActivity::class.java) {
                        putExtra(userEmail, email)
                        loggedUser
                    }
                }

            }
        }
    }
}