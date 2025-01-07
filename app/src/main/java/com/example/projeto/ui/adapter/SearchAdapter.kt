package com.example.projeto.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto.contextExpresions.loadImage
import com.example.projeto.databinding.SearchItemBinding
import com.example.projeto.model.Book
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class SearchAdapter(
    private val context: Context,
    livros: List<Book?> = emptyList(),
    var quandoClicado: (book: Book) -> Unit = {}
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private val livros = livros.toMutableList()

    class ViewHolder(
        private val binding: SearchItemBinding,
        var quandoClicado: (book: Book) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var livro: Book

        init {
            itemView.setOnClickListener {
                if (::livro.isInitialized)
                    quandoClicado(livro)
            }
        }

        fun vincula(book: Book) {
            this@ViewHolder.livro = book

            binding.title.text = book.title
            binding.image.loadImage(book.image)
            val autor = binding.writer
            when {
                book.author == "null" ->
                    autor.text = ""

                book.author.isNullOrEmpty() ->
                    autor.text = ""

                book.author.isNotBlank() ->
                    autor.text = book.author
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SearchItemBinding.inflate(
                LayoutInflater.from(context), parent, false
            ),
            quandoClicado
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val livro: Book? = livros[position]
        livro?.let {
            if (livro.title.isNotBlank())
                holder.vincula(it)
        }
    }

    override fun getItemCount(): Int = livros.size

    fun atualizar(livros: List<Book>) {
        CoroutineScope(IO).launch {
            val context = this@SearchAdapter
            notifyItemRangeRemoved(0, context.livros.size)
            context.livros.clear()
            context.livros.addAll(livros.requireNoNulls())
            notifyItemInserted(context.livros.size)
        }
    }
}
