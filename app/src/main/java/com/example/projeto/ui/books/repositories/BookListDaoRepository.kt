package com.example.projeto.ui.books.repositories

import com.example.projeto.model.SavedBook

interface BookListDaoRepository {

    fun showSavedBooks(emailUser: String): List<SavedBook>?

    fun removeBookFromBooklist(savedBook: SavedBook)

}