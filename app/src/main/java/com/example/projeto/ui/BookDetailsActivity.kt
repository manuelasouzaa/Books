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
import com.example.projeto.contextExpresions.userEmail
import com.example.projeto.databinding.BookDetailsBinding
import com.example.projeto.databinding.SavedBookDialogBinding
import com.example.projeto.model.Book
import com.example.projeto.viewModel.BookDetailsViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookDetailsActivity : UserActivity() {

    private val viewModel: BookDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = BookDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val emailUser: String = intent.getStringExtra(userEmail).toString()
        val book: Book = intent.getParcelableExtra<Book>(idBook) as Book

        bookDetailsConfig(binding, book, emailUser)

        binding.btnReturnBookDetailsActivity.setOnClickListener {
            finish()
        }

        binding.btnAddBookDetails.setOnClickListener {
            lifecycleScope.launch(IO) {
                val savedBook =
                    viewModel.fetchSavedBook(book, emailUser, this@BookDetailsActivity)

                withContext(Main) {
                    if (savedBook == null) {
                        addBook(viewModel, book, emailUser, binding)
                    }

                    if (savedBook !== null) {
                        toast("Este livro já foi adicionado")
                    }
                }
            }
        }
    }

    private fun addBook(
        viewModel: BookDetailsViewModel,
        book: Book,
        emailUser: String,
        binding: BookDetailsBinding
    ) {
        viewModel.addBook(book, emailUser, this@BookDetailsActivity)
        binding.btnAddBookDetails.setImageResource(R.drawable.ic_bookmark_added)
        showDialogBox(emailUser)
    }

    private fun showDialogBox(emailUser: String) {
        val dialog = Dialog(this)
        val bindingDialog = SavedBookDialogBinding.inflate(layoutInflater)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnReturnDialog = bindingDialog.btnReturnSavedBookDialog
        val btnBooklistDialog = bindingDialog.btnBooklistSavedBookDialog

        btnReturnDialog.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        btnBooklistDialog.setOnClickListener {
            dialog.dismiss()
            finish()
            goTo(FavoritesActivity::class.java) {
                putExtra(userEmail, emailUser)
            }
        }
        dialog.show()

        bindingDialog.btnCloseSavedBookDialog.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun bookDetailsConfig(
        binding: BookDetailsBinding,
        book: Book,
        emailUser: String
    ) {
        binding.bookTitleBookDetailsActivity.text = book.title
        binding.bookDescBookDetailsActivity.text = book.description
        binding.bookImageBookDetailsActivity.loadImage(book.image)

        when {
            book.author == "null" -> {
                binding.bookAuthorBookDetailsActivity.text = ""
            }

            book.author != "null" -> {
                binding.bookAuthorBookDetailsActivity.text = book.author.toString()
            }
        }

        lifecycleScope.launch(IO) {
            val savedBook =
                viewModel.fetchSavedBook(book, emailUser, this@BookDetailsActivity)

            if (savedBook !== null)
                binding.btnAddBookDetails.setImageResource(R.drawable.ic_bookmark_added)
        }
    }
}

