package com.example.projeto.ui

import android.os.Bundle
import android.view.View.GONE
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.contextExpresions.idBook
import com.example.projeto.contextExpresions.loggedUser
import com.example.projeto.contextExpresions.userEmail
import com.example.projeto.database.LibraryDatabase
import com.example.projeto.databinding.FavoritesActivityBinding
import com.example.projeto.ui.adapter.FavoritesAdapter
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesActivity : UserActivity() {

    private val binding by lazy {
        FavoritesActivityBinding.inflate(layoutInflater)
    }

    private val dao by lazy {
        LibraryDatabase.instance(this).savedBookDao()
    }

    private val adapter by lazy {
        FavoritesAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val emailUser = intent.getStringExtra(userEmail).toString()
        verifyList(emailUser)

        binding.btnReturnFavoritesActivity.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        val emailUser = intent.getStringExtra(userEmail).toString()
        verifyList(emailUser)
    }

    private fun verifyList(
        emailUser: String
    ) {
        val recycler = binding.recyclerFavoritesActivity

        lifecycleScope.launch(IO) {
            val savedBooks = dao.showSavedBooks(emailUser)

            withContext(Main) {

                if (savedBooks != null) {
                    recyclerViewConfig(emailUser, recycler)
                    adapter.update(savedBooks)
                    val booksQuantity = adapter.itemCount

                    if (booksQuantity == 1)
                        binding.bookQuantityFavoritesActivity.text = "1 livro adicionado"
                    if (booksQuantity > 1)
                        binding.bookQuantityFavoritesActivity.text = "$booksQuantity livros adicionados"
                }

                if (savedBooks.isNullOrEmpty()) {
                    binding.bookQuantityFavoritesActivity.text = "Nenhum livro adicionado"
                    recycler.visibility = GONE
                }
            }
        }
    }

    private fun recyclerViewConfig(emailUser: String, recycler: RecyclerView) {
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        adapter.whenItemIsClicked = { book ->
            goTo(SavedBookActivity::class.java) {
                putExtra(userEmail, emailUser)
                putExtra(idBook, book)
                loggedUser
            }
            finish()
        }
    }
}