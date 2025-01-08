package com.example.projeto.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto.contextExpresions.idLivro
import com.example.projeto.contextExpresions.irPara
import com.example.projeto.contextExpresions.toast
import com.example.projeto.contextExpresions.usuarioEmail
import com.example.projeto.contextExpresions.usuarioLogado
import com.example.projeto.databinding.SearchActivityBinding
import com.example.projeto.model.Book
import com.example.projeto.ui.adapter.SearchAdapter

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

        val email = intent.getStringExtra(usuarioEmail)
        val bookList: List<Book>? = intent.getSerializableExtra("booklist") as List<Book>?

        binding.btnVoltar.setOnClickListener {
            irPara(MainActivity::class.java) {
                usuarioLogado
            }
        }

        when {
            bookList.isNullOrEmpty() ->
                toast("Livro não encontrado")

            bookList.isNotEmpty() -> {
                recyclerViewConfig(email.toString())
                adapter.atualizar(bookList)
            }
        }
    }

    private fun recyclerViewConfig(email: String) {
        val recycler = binding.recycler
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        adapter.quandoClicado = { book ->
            irPara(BookDetailsActivity::class.java) {
                putExtra(usuarioEmail, email)
                putExtra(idLivro, book)
                usuarioLogado
            }
        }
    }
}