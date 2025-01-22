package com.example.projeto.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.lifecycle.lifecycleScope
import com.example.projeto.contextExpresions.idBook
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.contextExpresions.loadImage
import com.example.projeto.contextExpresions.userEmail
import com.example.projeto.contextExpresions.loggedUser
import com.example.projeto.database.LibraryDatabase
import com.example.projeto.databinding.DeleteBookDialogBinding
import com.example.projeto.databinding.SavedBookActivityBinding
import com.example.projeto.model.SavedBook
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class SavedBookActivity : UserActivity() {

    private val binding by lazy {
        SavedBookActivityBinding.inflate(layoutInflater)
    }

    private val dao by lazy {
        LibraryDatabase.instance(this).savedBookDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val savedBook = intent.getParcelableExtra<SavedBook>(idBook) as SavedBook
        val emailUser = intent.getStringExtra(userEmail)

        savedBookConfig(savedBook)

        binding.btnReturnSavedBookActivity.setOnClickListener {
            goToBooklist(emailUser)
        }

        binding.btnRemoveSavedBookActivity.setOnClickListener {
            showDialogBox(savedBook, emailUser)
        }
    }

    private fun savedBookConfig(savedBook: SavedBook) {
        binding.bookTitleSavedBookActivity.text = savedBook.title
        binding.bookImageSavedBookActivity.loadImage(savedBook.image)

        if (savedBook.author.isNullOrEmpty())
            binding.bookAuthorSavedBookActivity.text = ""
        if (savedBook.author == "null")
            binding.bookAuthorSavedBookActivity.text = ""
        if (savedBook.author != "null")
            binding.bookAuthorSavedBookActivity.text = savedBook.author

        if (savedBook.description.isNullOrEmpty())
            binding.bookDescSavedBookActivity.text = ""
        if (savedBook.description == "null")
            binding.bookDescSavedBookActivity.text = ""
        if (savedBook.description != "null")
            binding.bookDescSavedBookActivity.text = savedBook.description
    }

    private fun goToBooklist(emailUser: String?) {
        goTo(FavoritesActivity::class.java) {
            putExtra(userEmail, emailUser)
            loggedUser
        }
        finish()
    }

    private fun showDialogBox(savedBook: SavedBook, emailUser: String?) {
        val dialog = Dialog(this)
        val bindingDialog = DeleteBookDialogBinding.inflate(layoutInflater)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnYes = bindingDialog.btnSimDeleteBookDialog
        val btnNo = bindingDialog.btnNaoDeleteBookDialog

        btnNo.setOnClickListener {
            dialog.dismiss()
        }

        btnYes.setOnClickListener {
            dialog.dismiss()
            removeBook(savedBook, emailUser)
        }
        dialog.show()
    }

    private fun removeBook(
        savedBook: SavedBook,
        emailUser: String?
    ) {
        lifecycleScope.launch(IO) {
            dao.removeSavedBook(savedBook)
            finish()
            goToBooklist(emailUser)
        }
    }
}