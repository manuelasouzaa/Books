package com.example.projeto.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto.contextExpresions.loadImage
import com.example.projeto.databinding.SearchItemBinding
import com.example.projeto.model.SavedBook
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class FavoritesAdapter(
    private val context: Context,
    livros: List<SavedBook?> = emptyList(),
    var quandoClicado: (book: SavedBook) -> Unit = {}
) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    private val livros = livros.toMutableList()

    class ViewHolder(
        private val binding: SearchItemBinding,
        var quandoClicado: (book: SavedBook) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var livro: SavedBook

        init {
            itemView.setOnClickListener {
                if (::livro.isInitialized)
                    quandoClicado(livro)
            }
        }

        fun vincula(savedBook: SavedBook) {
            this@ViewHolder.livro = savedBook

            binding.title.text = savedBook.title
            binding.image.loadImage(savedBook.image)
            val autor = binding.writer
            when {
                savedBook.author == "null" ->
                    autor.text = ""

                savedBook.author?.isEmpty() == true ->
                    autor.text = ""

                savedBook.author?.isNotBlank() == true ->
                    autor.text = savedBook.author
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
        val livro: SavedBook? = livros[position]
        livro?.let {
            if (livro.title.isNotBlank())
                holder.vincula(it)
        }
    }

    override fun getItemCount(): Int = livros.size

    fun atualizar(livrosSalvos: List<SavedBook>) {

        val context = this@FavoritesAdapter
        notifyItemRangeRemoved(0, context.livros.size)
        context.livros.clear()
        context.livros.addAll(livrosSalvos)
        notifyItemInserted(context.livros.size)

    }
}