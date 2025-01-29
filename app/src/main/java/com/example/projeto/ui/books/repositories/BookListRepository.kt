package com.example.projeto.ui.books.repositories

import android.content.Context
import com.example.projeto.database.LibraryDatabase
import com.example.projeto.model.SavedBook

class BookListRepository(context: Context) : BookListDaoRepository {

    private val database = LibraryDatabase.instance(context)
    private val savedBookDao = database.savedBookDao()

    override fun showSavedBooks(emailUser: String): List<SavedBook>? {
        return savedBookDao.showSavedBooks(emailUser)
    }

    override fun removeBookFromBooklist(savedBook: SavedBook) {
        savedBookDao.removeSavedBook(savedBook)
    }

}