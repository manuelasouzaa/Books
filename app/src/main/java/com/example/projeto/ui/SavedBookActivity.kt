package com.example.projeto.ui

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.projeto.contextExpresions.bookId
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.contextExpresions.loadImage
import com.example.projeto.contextExpresions.loggedUser
import com.example.projeto.contextExpresions.toast
import com.example.projeto.contextExpresions.userEmail
import com.example.projeto.database.LibraryDatabase
import com.example.projeto.databinding.SavedBookActivityBinding
import com.example.projeto.model.SavedBook
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedBookActivity: UserActivity() {

    private val binding by lazy {
        SavedBookActivityBinding.inflate(layoutInflater)
    }

    private val dao by lazy {
        LibraryDatabase.instance(this).savedBookDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val favoriteBook = intent.getParcelableExtra<SavedBook>(bookId) as SavedBook
        val emailUser = intent.getStringExtra(userEmail)

        binding.btnReturn.setOnClickListener {
            goToBookList(emailUser)
        }

        binding.bookTitle.text = favoriteBook.title
        binding.bookAuthor.text = favoriteBook.author.toString()
        binding.bookDesc.text = favoriteBook.description.toString()
        binding.bookImage.loadImage(favoriteBook.image)

        binding.btnRemove.setOnClickListener {
            removeFromBooklist(favoriteBook, emailUser)
        }
    }

    private fun removeFromBooklist(
        favoriteBook: SavedBook,
        emailUser: String?
    ) {
        lifecycleScope.launch(IO) {
            dao.deleteSavedBook(favoriteBook)
            withContext(Main) {
                toast("Livro excluído com sucesso")
                goToBookList(emailUser)
            }
        }
    }

    private fun goToBookList(emailUser: String?) {
        goTo(FavoritesActivity::class.java) {
            putExtra(userEmail, emailUser)
            loggedUser
        }
        finish()
    }
}