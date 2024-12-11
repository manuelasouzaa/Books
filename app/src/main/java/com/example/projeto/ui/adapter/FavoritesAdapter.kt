package com.example.projeto.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto.contextExpresions.loadImage
import com.example.projeto.databinding.FavoriteItemBinding
import com.example.projeto.model.SavedBook
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class FavoritesAdapter(
    private val context: Context,
    livros: List<SavedBook?> = emptyList(),
    var whenItemIsClicked: (book: SavedBook) -> Unit = {}
): RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    private val books = livros.toMutableList()

    class ViewHolder (
        private val binding: FavoriteItemBinding,
        var whenItemIsClicked: (book: SavedBook) -> Unit,
    ): RecyclerView.ViewHolder(binding.root) {

        fun vincula(savedBook: SavedBook) {
            CoroutineScope(IO).launch {
                this@ViewHolder.book = savedBook

                binding.title.text = savedBook.title
                binding.image.loadImage(savedBook.image)
                val author = binding.writer
                when {
                    book.author == "null" -> {
                        author.text = ""
                    }
                    book.author?.isEmpty() == true -> {
                        author.text = ""
                    }
                    book.author?.isNotBlank() == true -> {
                        author.text = book.author
                    }
                }
            }
        }

        private lateinit var book: SavedBook

        init {
            itemView.setOnClickListener {
                if (::book.isInitialized) {
                    whenItemIsClicked(book)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FavoriteItemBinding.inflate(
                LayoutInflater.from(context), parent, false
            ),
            whenItemIsClicked
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book: SavedBook? = books[position]
        book?.let {
            if (book.title.isNotBlank()) {
                holder.vincula(it)
            }
        }
    }

    override fun getItemCount(): Int = books.size
}