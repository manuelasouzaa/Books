package com.example.projeto.ui.user

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.projeto.expresions.goTo
import com.example.projeto.databinding.AccountActivityBinding
import com.example.projeto.ui.lists.FavoritesActivity
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountActivity : BaseUserActivity() {

    private val binding by lazy {
        AccountActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        verifyLoggedUser(this@AccountActivity)

        lifecycleScope.launch(IO) {
            replaceTexts()
        }

        binding.btnLogout.setOnClickListener {
            viewModel.removeUser(this)
            goToLogin()
        }

        binding.btnReturnAccountActivity.setOnClickListener {
            goTo(MainActivity::class.java)
            finish()
        }

        binding.btnBooklistAccountActivity.setOnClickListener {
            goTo(FavoritesActivity::class.java)
        }
    }

    private suspend fun replaceTexts() {
        val user = viewModel.getLoggedUser(this)
        user?.collect {
            withContext(Main) {
                binding.userEmailAccountActivity.text = it.email
                binding.usernameAccountActivity.text = it.name
            }
        }
    }
}