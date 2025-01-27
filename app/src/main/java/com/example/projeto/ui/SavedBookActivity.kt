package com.example.projeto.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.contextExpresions.idBook
import com.example.projeto.contextExpresions.loadImage
import com.example.projeto.database.LibraryDatabase
import com.example.projeto.database.Repository
import com.example.projeto.databinding.DeleteBookDialogBinding
import com.example.projeto.databinding.SavedBookActivityBinding
import com.example.projeto.model.SavedBook
import com.example.projeto.viewModel.MainViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class SavedBookActivity : UserActivity() {

    private val binding by lazy {
        SavedBookActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val savedBook = intent.getParcelableExtra<SavedBook>(idBook) as SavedBook

        verifyLoggedUser(this)

        savedBookConfig(savedBook)

        binding.btnReturnSavedBookActivity.setOnClickListener {
            goToBooklist()
        }
        binding.btnRemoveSavedBookActivity.setOnClickListener {
            showDialogBox(savedBook)
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

    private fun goToBooklist() {
        goTo(FavoritesActivity::class.java)
        finish()
    }

    private fun showDialogBox(savedBook: SavedBook) {
        val dialog = Dialog(this)
        val bindingDialog = DeleteBookDialogBinding.inflate(layoutInflater)

        dialogConfig(dialog, bindingDialog)

        bindingDialog.btnNaoDeleteBookDialog.setOnClickListener {
            dialog.dismiss()
        }
        bindingDialog.btnSimDeleteBookDialog.setOnClickListener {
            dialog.dismiss()
            removeBook(savedBook)
        }
        dialog.show()
    }

    private fun dialogConfig(dialog: Dialog, bindingDialog: DeleteBookDialogBinding) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun removeBook(savedBook: SavedBook) {
        lifecycleScope.launch(IO) {
            Repository(this@SavedBookActivity).removeBookFromBooklist(savedBook)
        }
        finish()
        goToBooklist()
    }
}