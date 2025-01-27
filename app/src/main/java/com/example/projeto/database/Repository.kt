package com.example.projeto.database

import android.content.Context
import com.example.projeto.model.SavedBook
import com.example.projeto.model.User
import kotlinx.coroutines.flow.Flow

class Repository(context: Context) {

    private val database = LibraryDatabase.instance(context)
    private val userDao = database.userDao()
    private val savedBookDao = database.savedBookDao()

    fun fetchUserByEmail(email: String): Flow<User>? {
        return userDao.fetchUserByEmail(email)
    }

    fun saveNewUser(newUser: User) {
        userDao.save(newUser)
    }

    fun searchUser(email: String, password: String): User? {
        return userDao.searchUser(email, password)
    }

    fun showSavedBooks(emailUser: String): List<SavedBook>? {
        return savedBookDao.showSavedBooks(emailUser)
    }

    fun removeBookFromBooklist(savedBook: SavedBook) {
        savedBookDao.removeSavedBook(savedBook)
    }

}

