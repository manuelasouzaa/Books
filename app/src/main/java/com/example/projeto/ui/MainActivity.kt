package com.example.projeto.ui

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.contextExpresions.toast
import com.example.projeto.databinding.ActivityMainBinding
import com.example.projeto.model.User
import com.example.projeto.viewModel.MainActivityViewModel
import com.example.projeto.viewModel.MainViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.Serializable

class MainActivity : UserActivity() {

    private val mainViewModel: MainActivityViewModel by viewModels()
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        verifyLoggedUser(this)

        binding.btnSearchMainActivity.setOnClickListener {
            search()
        }
        binding.btnAccountMainActivity.setOnClickListener {
            goTo(AccountActivity::class.java)
            finish()
        }
        binding.btnBooklistMainActivity.setOnClickListener {
            goTo(FavoritesActivity::class.java)
        }
    }

    private fun search() {
        binding.loadingMainActivity.visibility = VISIBLE
        binding.btnSearchMainActivity.visibility = GONE

        val search = binding.editTextMainActivity.text.toString()

        try {
            lifecycleScope.launch {
                mainViewModel.searchBook(search)

                mainViewModel.message.collect {
                    if (it == "Livro não encontrado")
                        errorInSearch()

                    if (it == "Livros encontrados") {
                        mainViewModel.booklist.collect {
                            goTo(SearchActivity::class.java) {
                                putExtra("booklist", it as Serializable)
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
}