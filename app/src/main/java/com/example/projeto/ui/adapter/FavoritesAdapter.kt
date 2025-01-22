package com.example.projeto.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projeto.contextExpresions.loadImage
import com.example.projeto.databinding.SearchItemBinding
import com.example.projeto.model.SavedBook

class FavoritesAdapter(
    private val context: Context,
    books: List<SavedBook?> = emptyList(),
    var whenItemIsClicked: (book: SavedBook) -> Unit = {}
) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    private val books = books.toMutableList()

    class ViewHolder(
        private val binding: SearchItemBinding,
        var whenItemIsClicked: (book: SavedBook) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var book: SavedBook

        init {
            itemView.setOnClickListener {
                if (::book.isInitialized)
                    whenItemIsClicked(book)
            }
        }

        fun bind(savedBook: SavedBook) {
            this@ViewHolder.book = savedBook

            binding.titleSearchItem.text = savedBook.title
            binding.imageSearchItem.loadImage(savedBook.image)
            val author = binding.writerSearchItem
            when {
                savedBook.author == "null" ->
                    author.text = ""

                savedBook.author?.isEmpty() == true ->
                    author.text = ""

                savedBook.author?.isNotBlank() == true ->
                    author.text = savedBook.author
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SearchItemBinding.inflate(
                LayoutInflater.from(context), parent, false
            ),
            whenItemIsClicked
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book: SavedBook? = books[position]
        book?.let {
            if (book.title.isNotBlank())
                holder.bind(it)
        }
    }

    override fun getItemCount(): Int = books.size

    fun update(livrosSalvos: List<SavedBook>) {
        val context = this@FavoritesAdapter
        notifyItemRangeRemoved(0, context.books.size)
        context.books.clear()
        context.books.addAll(livrosSalvos)
        notifyItemInserted(context.books.size)
    }
}