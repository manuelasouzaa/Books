package com.example.projeto.ui.user

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.projeto.expresions.goTo
import com.example.projeto.expresions.toast
import com.example.projeto.databinding.ActivityMainBinding
import com.example.projeto.ui.lists.FavoritesActivity
import com.example.projeto.ui.lists.SearchActivity
import com.example.projeto.viewModel.MainActivityViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.io.Serializable

class MainActivity : BaseUserActivity() {

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
            verifySearch()
        }
        binding.btnAccountMainActivity.setOnClickListener {
            goTo(AccountActivity::class.java)
            finish()
        }
        binding.btnBooklistMainActivity.setOnClickListener {
            goTo(FavoritesActivity::class.java)
        }
    }

    private fun verifySearch() {
        val search = binding.editTextMainActivity.text.toString()
        Log.i("TAG", "verifySearch: $search")
        if (search == "") {
            toast("Pesquise um título ou autor")
            return
        }
        if (search !== "") {
            search(search)
        }
    }

    private fun search(search: String) {
        binding.loadingMainActivity.visibility = VISIBLE
        binding.btnSearchMainActivity.visibility = GONE

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