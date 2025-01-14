package com.example.projeto.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projeto.database.LibraryDatabase
import com.example.projeto.model.Book
import com.example.projeto.model.SavedBook
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.UUID

class BookDetailsViewModel : ViewModel() {

    fun addBook(book: Book, emailUser: String, context: Context) {
        viewModelScope.launch(IO) {
            val bookToSave =
                SavedBook(
                    image = book.image.toString(),
                    author = book.author,
                    idBook = book.id,
                    description = book.description.toString(),
                    userEmail = emailUser,
                    id = UUID.randomUUID().toString(),
                    title = book.title.toString()
                )

            val dao = LibraryDatabase.instance(context).savedBookDao()
            dao.saveBook(bookToSave)
        }
    }

    fun fetchSavedBook(book: Book, emailUser: String, context: Context): SavedBook? {
        val dao = LibraryDatabase.instance(context).savedBookDao()
        return dao.fetchSavedBook(book.id, emailUser)
    }
}