package com.example.projeto.ui

import android.os.Bundle
import android.view.View.GONE
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto.contextExpresions.idLivro
import com.example.projeto.contextExpresions.irPara
import com.example.projeto.contextExpresions.usuarioLogado
import com.example.projeto.contextExpresions.usuarioEmail
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

        val emailUsuario = intent.getStringExtra(usuarioEmail).toString()

        verificarLista(emailUsuario)

        binding.btnVoltar.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        val emailUsuario = intent.getStringExtra(usuarioEmail).toString()
        verificarLista(emailUsuario)
    }

    private fun verificarLista(
        emailUsuario: String
    ) {
        val recycler = binding.recycler
        lifecycleScope.launch(IO) {
            val livrosSalvos = dao.exibirLivrosSalvos(emailUsuario)

            if (livrosSalvos != null)
                withContext(Main) {
                    recyclerViewConfig(emailUsuario, recycler)
                    adapter.atualizar(livrosSalvos)

                    val quantidadeLivros = adapter.itemCount
                    if (quantidadeLivros == 1)
                        binding.bookQuantity.text = "1 livro adicionado"
                    if (quantidadeLivros > 1)
                        binding.bookQuantity.text = "$quantidadeLivros livros adicionados"
                }

            if (livrosSalvos.isNullOrEmpty())
                withContext(Main) {
                    binding.bookQuantity.text = "Nenhum livro adicionado"
                    recycler.visibility = GONE
                }
        }
    }

    private fun recyclerViewConfig(emailUser: String, recycler: RecyclerView) {
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        adapter.quandoClicado = { book ->
            irPara(SavedBookActivity::class.java) {
                putExtra(usuarioEmail, emailUser)
                putExtra(idLivro, book)
                usuarioLogado
            }
            finish()
        }
    }
}