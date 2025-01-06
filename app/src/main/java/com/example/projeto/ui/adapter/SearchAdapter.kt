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
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch

class SearchAdapter(
    private val context: Context,
    livros: List<Book?> = emptyList(),
    var whenItemIsClicked: (book: Book) -> Unit = {}
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private val books = livros.toMutableList()

    class ViewHolder(
        private val binding: SearchItemBinding,
        var whenItemIsClicked: (book: Book) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var book: Book

        init {
            itemView.setOnClickListener {
                if (::book.isInitialized)
                    whenItemIsClicked(book)
            }
        }

        fun vincula(book: Book) {
            CoroutineScope(Main).launch {
                this@ViewHolder.book = book
                binding.title.text = book.title
                binding.image.loadImage(book.image)

                val author = binding.writer
                when {
                    book.author == "null" ->
                        author.text = ""

                    book.author.isEmpty() ->
                        author.text = ""

                    book.author.isNotBlank() ->
                        author.text = book.author
                }
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
        val book: Book? = books[position]
        book?.let {
            if (book.title.isNotBlank())
                holder.vincula(it)
        }
    }

    override fun getItemCount(): Int = books.size

    fun update(books: List<Book>) {
        CoroutineScope(IO).launch {
            val context = this@SearchAdapter
            notifyItemRangeRemoved(0, context.books.size)
            context.books.clear()
            context.books.addAll(books.requireNoNulls())
            notifyItemInserted(context.books.size)
        }
    }
}
