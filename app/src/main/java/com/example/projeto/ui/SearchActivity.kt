package com.example.projeto.ui

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto.contextExpresions.bookId
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.contextExpresions.toast
import com.example.projeto.contextExpresions.userEmail
import com.example.projeto.databinding.SearchActivityBinding
import com.example.projeto.model.Book
import com.example.projeto.ui.adapter.SearchAdapter
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
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

        val email = user.value?.email.toString()

        Log.i("User", "onCreate: ${user.value.toString()}")
        Log.i("teste", "onCreate: $email")

        val bookList: List<Book>? = intent.getSerializableExtra("booklist") as List<Book>?

        when {
            bookList.isNullOrEmpty() -> {
                toast("Livro não encontrado")
            }
            bookList.isNotEmpty() -> {
                lifecycleScope.launch(IO) {
                    recyclerViewConfig(email)
                    adapter.update(bookList)
                }
            }
        }
    }

    private fun recyclerViewConfig(email: String) {
        val recycler = binding.recycler
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        adapter.whenItemIsClicked = { book ->
            lifecycleScope.launch(Main) {
                goTo(BookDetailsActivity::class.java) {
                    putExtra(userEmail, email)
                    Log.i("it worked", "recyclerViewConfig: $email")
                    putExtra(bookId, book)
                }
            }
        }
    }
}