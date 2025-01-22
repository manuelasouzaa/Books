package com.example.projeto.ui

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.contextExpresions.loggedUser
import com.example.projeto.contextExpresions.toast
import com.example.projeto.contextExpresions.userEmail
import com.example.projeto.databinding.ActivityMainBinding
import com.example.projeto.viewModel.MainActivityViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable

class MainActivity : UserActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        lifecycleScope.launch(IO) {
            user.filterNotNull().collect {
                val email = user.value?.email.toString()

                withContext(Main){
                    binding.btnSearchMainActivity.setOnClickListener {
                        search(email)
                    }
                    binding.btnAccountMainActivity.setOnClickListener {
                        goToActivity(AccountActivity::class.java, email)
                        finish()
                    }

                    binding.btnBooklistMainActivity.setOnClickListener {
                        goToActivity(FavoritesActivity::class.java, email)
                    }
                }
            }
        }
    }

    private fun search(email: String) {
        binding.loadingMainActivity.visibility = VISIBLE
        binding.btnSearchMainActivity.visibility = GONE

        val search = binding.editTextMainActivity.text.toString()

        try {
            lifecycleScope.launch {
                viewModel.searchBook(search)

                viewModel.message.collect {
                    if (it == "Livro não encontrado")
                        errorInSearch()

                    if (it == "Livros encontrados") {
                        viewModel.booklist.collect {
                            goTo(SearchActivity::class.java) {
                                putExtra("booklist", it as Serializable)
                                putExtra(userEmail, email)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            errorInSearch()
        }
        binding.editTextMainActivity.text.clear()
    }

    private fun errorInSearch() {
        lifecycleScope.launch(Main) {
            toast("Erro ao pesquisar")
            binding.loadingMainActivity.visibility = GONE
            binding.btnSearchMainActivity.visibility = VISIBLE
        }
    }

    private fun goToActivity(activity: Class<*>, email: String) {
        goTo(activity) {
            putExtra(userEmail, email)
            loggedUser
        }
    }
}