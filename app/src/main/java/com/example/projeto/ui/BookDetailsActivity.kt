package com.example.projeto.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.projeto.R
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.contextExpresions.idBook
import com.example.projeto.contextExpresions.loadImage
import com.example.projeto.contextExpresions.toast
import com.example.projeto.databinding.BookDetailsBinding
import com.example.projeto.databinding.SavedBookDialogBinding
import com.example.projeto.model.Book
import com.example.projeto.viewModel.BookDetailsViewModel
import com.example.projeto.viewModel.MainViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class BookDetailsActivity : UserActivity() {

    private val bookDetailsViewModel: BookDetailsViewModel by viewModels()
    private val binding by lazy {
        BookDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        verifyLoggedUser(this@BookDetailsActivity)

        val book: Book = intent.getParcelableExtra<Book>(idBook) as Book
        lateinit var emailUser: String

        runBlocking {
            viewModel.getUserEmail(this@BookDetailsActivity)?.let {
                emailUser = it
            }
        }

        bookDetailsConfig(book, emailUser)

        binding.btnReturnBookDetailsActivity.setOnClickListener {
            finish()
        }

        binding.btnAddBookDetails.setOnClickListener {
            lifecycleScope.launch(IO) {
                addBookToBooklist(book, emailUser)
            }
        }
    }

    private fun bookDetailsConfig(book: Book, emailUser: String) {
        binding.bookTitleBookDetailsActivity.text = book.title
        binding.bookDescBookDetailsActivity.text = book.description
        binding.bookImageBookDetailsActivity.loadImage(book.image)

        if (book.author == "null")
            binding.bookAuthorBookDetailsActivity.text = ""

        if (book.author != "null")
            binding.bookAuthorBookDetailsActivity.text = book.author.toString()

        lifecycleScope.launch(IO) {
            val savedBook = bookDetailsViewModel.fetchSavedBook(
                book, emailUser, this@BookDetailsActivity
            )
            if (savedBook !== null)
                binding.btnAddBookDetails.setImageResource(R.drawable.ic_bookmark_added)
        }
    }

    private suspend fun addBookToBooklist(book: Book, emailUser: String) {
        val savedBook = bookDetailsViewModel.fetchSavedBook(
            book, emailUser, this@BookDetailsActivity
        )

        withContext(Main) {
            if (savedBook == null)
                addBook(book, emailUser, binding)

            if (savedBook !== null)
                toast("Este livro já foi adicionado")
        }
    }

    private fun addBook(book: Book, emailUser: String, binding: BookDetailsBinding) {
        bookDetailsViewModel.addBook(book, emailUser, this@BookDetailsActivity)
        binding.btnAddBookDetails.setImageResource(R.drawable.ic_bookmark_added)
        showDialogBox()
    }

    private fun showDialogBox() {
        val dialog = Dialog(this)
        val bindingDialog = SavedBookDialogBinding.inflate(layoutInflater)

        dialogConfig(dialog, bindingDialog)

        bindingDialog.btnReturnSavedBookDialog.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        bindingDialog.btnBooklistSavedBookDialog.setOnClickListener {
            dialog.dismiss()
            finish()
            goTo(FavoritesActivity::class.java)
        }
        bindingDialog.btnCloseSavedBookDialog.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun dialogConfig(
        dialog: Dialog,
        bindingDialog: SavedBookDialogBinding,
    ) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}

