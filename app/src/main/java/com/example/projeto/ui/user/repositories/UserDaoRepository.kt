package com.example.projeto.ui.user.repositories

import android.content.Context
import com.example.projeto.database.LibraryDatabase
import com.example.projeto.model.User
import kotlinx.coroutines.flow.Flow

class UserDaoRepository(context: Context) : UserRepository {

    private val database = LibraryDatabase.instance(context)
    private val userDao = database.userDao()

    override fun fetchUserByEmail(email: String): Flow<User>? {
        return userDao.fetchUserByEmail(email)
    }

    override fun saveNewUser(newUser: User) {
        userDao.save(newUser)
    }

    override fun searchUser(email: String, password: String): User? {
        return userDao.searchUser(email, password)
    }
}