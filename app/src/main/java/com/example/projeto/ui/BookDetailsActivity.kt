package com.example.projeto.ui

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.projeto.R
import com.example.projeto.contextExpresions.bookId
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.contextExpresions.loadImage
import com.example.projeto.contextExpresions.loggedUser
import com.example.projeto.contextExpresions.toast
import com.example.projeto.contextExpresions.userEmail
import com.example.projeto.database.LibraryDatabase
import com.example.projeto.databinding.BookDetailsBinding
import com.example.projeto.model.Book
import com.example.projeto.model.SavedBook
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class BookDetailsActivity : UserActivity() {

    private val dao by lazy {
        LibraryDatabase.instance(this).savedBookDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = BookDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val email: String = intent.getStringExtra(userEmail).toString()
        val book: Book = intent.getParcelableExtra<Book>(bookId) as Book
        lifecycleScope.launch(IO) {
            val existentBook: SavedBook? = dao.searchSavedBook(book.id, email)
            bookDetailsConfig(binding, book, existentBook)
        }

        binding.btnReturn.setOnClickListener {
            finish()
        }

        val bookToSave =
            SavedBook(
                image = book.image.toString(),
                author = book.author,
                idBook = book.id,
                description = book.description.toString(),
                userEmail = email,
                id = UUID.randomUUID().toString(),
                title = book.title
            )

        val btnSave = binding.btnSave
        btnSave.setOnClickListener {
            lifecycleScope.launch(IO) {
                if (bookToSave.userEmail == "null") {
                    withContext(Main) {
                        toast("Não foi possível salvar o livro")
                    }
                }

                if (bookToSave.userEmail != "null") {

                    val existentBook: SavedBook? = dao.searchSavedBook(book.id, email)
                    when {

                        existentBook == null -> {
                            dao.saveBook(bookToSave)
                            withContext(Main) {
                                toast("Adicionado à BookList!")
                                binding.btnSave.setImageResource(R.drawable.ic_bookmark_added)
                            }
                        }

                        true -> {
                            withContext(Main) {
                                toast("Este livro já foi adicionado")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun bookDetailsConfig(
        binding: BookDetailsBinding,
        book: Book,
        existentBook: SavedBook?
    ) {
        binding.bookTitle.text = book.title
        binding.bookDesc.text = book.description
        binding.bookImage.loadImage(book.image)
        binding.bookAuthor.text = book.author

        if (existentBook !== null) {
            binding.btnSave.setImageResource(R.drawable.ic_bookmark_added)
        }
    }
}

