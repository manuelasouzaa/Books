package com.example.projeto.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.contextExpresions.idBook
import com.example.projeto.contextExpresions.toast
import com.example.projeto.databinding.SearchActivityBinding
import com.example.projeto.model.Book
import com.example.projeto.ui.adapter.SearchAdapter
import com.example.projeto.viewModel.MainViewModel
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

        verifyLoggedUser(this)

        val bookList: List<Book>? = intent.getSerializableExtra("booklist") as List<Book>?

        binding.btnReturnSearchActivity.setOnClickListener {
            goTo(MainActivity::class.java)
        }

        when {
            bookList.isNullOrEmpty() ->
                toast("Livro não encontrado")

            bookList.isNotEmpty() -> {
                recyclerViewConfig()
                adapter.update(bookList)
            }
        }
    }

    private fun recyclerViewConfig() {
        val recycler = binding.recyclerSearchActivity
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        adapter.whenItemIsClicked = { book ->
            goTo(BookDetailsActivity::class.java) {
                putExtra(idBook, book)
            }
        }
    }
}