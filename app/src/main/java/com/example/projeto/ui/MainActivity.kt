package com.example.projeto.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.contextExpresions.loggedUser
import com.example.projeto.contextExpresions.userEmail
import com.example.projeto.databinding.ActivityMainBinding
import com.example.projeto.viewModel.MainActivityViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : UserActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel = MainActivityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.btnAccount.setOnClickListener {
            goToActivity(AccountActivity::class.java)
            finish()
        }

        binding.btnFavorites.setOnClickListener {
            goToActivity(FavoritesActivity::class.java)
        }

        binding.search.setOnClickListener {
            val search = binding.editText.text.toString()
            lifecycleScope.launch(IO) {
                launch {
                    viewModel.searchApi(search, this@MainActivity, user)
                }
            }
        }
    }

    private fun goToActivity(activity: Class<*>) {
        lifecycleScope.launch(IO) {
            user.filterNotNull().collect {
                val email = user.value?.email.toString()
                withContext(Main){
                    goTo(activity) {
                        putExtra(userEmail, email)
                        loggedUser
                    }
                }
            }
        }
    }
}