package com.example.projeto.ui

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto.contextExpresions.bookId
import com.example.projeto.contextExpresions.goTo
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

    private val adapter = FavoritesAdapter(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val emailUser = intent.getStringExtra(userEmail).toString()

        verifyList(emailUser)

        binding.btnReturn.setOnClickListener {
            finish()
        }
    }

    private fun verifyList(
        emailUser: String
    ) {
        val recycler = binding.recycler
        lifecycleScope.launch(IO) {
            val savedBooks = dao.showSavedBooks(emailUser)
            if (savedBooks != null)
                withContext(Main) {
                    recyclerViewConfig(emailUser, recycler)
                    adapter.update(savedBooks)
                }

            if (savedBooks.isNullOrEmpty())
                withContext(Main) {
                    binding.noBooks.visibility = VISIBLE
                    recycler.visibility = GONE
                }
        }
    }

    private fun recyclerViewConfig(emailUser: String, recycler: RecyclerView) {
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(this)
        adapter.whenItemIsClicked = { book ->
            goTo(SavedBookActivity::class.java) {
                putExtra(userEmail, emailUser)
                putExtra(bookId, book)
                loggedUser
            }
            finish()
        }
    }
}