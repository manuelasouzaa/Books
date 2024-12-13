package com.example.projeto.ui

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto.contextExpresions.bookId
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.contextExpresions.loggedUser
import com.example.projeto.contextExpresions.toast
import com.example.projeto.contextExpresions.userEmail
import com.example.projeto.databinding.SearchActivityBinding
import com.example.projeto.model.Book
import com.example.projeto.ui.adapter.SearchAdapter
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class SearchActivity : UserActivity() {

    private val binding by lazy {
        SearchActivityBinding.inflate(layoutInflater)
    }

    private val adapter by lazy {
        SearchAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val email = intent.getStringExtra(userEmail)

        val bookList: List<Book>? = intent.getSerializableExtra("booklist") as List<Book>?

        binding.btnReturn.setOnClickListener {
            goTo(MainActivity::class.java) {
                loggedUser
            }
        }

        when {
            bookList.isNullOrEmpty() ->
                toast("Livro não encontrado")

            bookList.isNotEmpty() ->
                lifecycleScope.launch(IO) {
                    recyclerViewConfig(email.toString())
                    adapter.update(bookList)
                }

        }
    }

    private fun recyclerViewConfig(email: String) {
        val recycler = binding.recycler
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        adapter.whenItemIsClicked = { book ->
            goTo(BookDetailsActivity::class.java) {
                putExtra(userEmail, email)
                putExtra(bookId, book)
                loggedUser
            }
        }
    }
}