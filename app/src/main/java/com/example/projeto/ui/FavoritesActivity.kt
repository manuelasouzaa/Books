package com.example.projeto.ui

import android.os.Bundle
import android.view.View.GONE
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.contextExpresions.idBook
import com.example.projeto.database.LibraryDatabase
import com.example.projeto.database.Repository
import com.example.projeto.databinding.FavoritesActivityBinding
import com.example.projeto.model.SavedBook
import com.example.projeto.ui.adapter.FavoritesAdapter
import com.example.projeto.viewModel.MainViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesActivity : UserActivity() {

    private val binding by lazy {
        FavoritesActivityBinding.inflate(layoutInflater)
    }
    private val adapter by lazy {
        FavoritesAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        verifyLoggedUser(this)

        listConfig()

        binding.btnReturnFavoritesActivity.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        listConfig()
    }

    private fun listConfig() {
        lifecycleScope.launch {
            val emailUser = viewModel.getUserEmail(this@FavoritesActivity)
            if (emailUser !== null)
                verifyList(emailUser)
        }
    }

    private fun verifyList(emailUser: String) {
        val recycler = binding.recyclerFavoritesActivity

        lifecycleScope.launch(IO) {
            val savedBooks = Repository(this@FavoritesActivity).showSavedBooks(emailUser)
            withContext(Main) {
                if (savedBooks != null) {
                    configView(recycler, savedBooks)
                }
                if (savedBooks.isNullOrEmpty()) {
                    binding.bookQuantityFavoritesActivity.text = "Nenhum livro adicionado"
                    recycler.visibility = GONE
                }
            }
        }
    }

    private fun configView(recycler: RecyclerView, savedBooks: List<SavedBook>) {
        recyclerViewConfig(recycler)
        adapter.update(savedBooks)
        val booksQuantity = adapter.itemCount

        if (booksQuantity == 1)
            binding.bookQuantityFavoritesActivity.text = "1 livro adicionado"
        if (booksQuantity > 1)
            binding.bookQuantityFavoritesActivity.text = "$booksQuantity livros adicionados"
    }

    private fun recyclerViewConfig(recycler: RecyclerView) {
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        adapter.whenItemIsClicked = { book ->
            goTo(SavedBookActivity::class.java) {
                putExtra(idBook, book)
            }
            finish()
        }
    }
}