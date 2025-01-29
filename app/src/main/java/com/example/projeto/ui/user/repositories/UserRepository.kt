package com.example.projeto.ui.user.repositories

import com.example.projeto.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun fetchUserByEmail(email: String): Flow<User>?

    fun saveNewUser(newUser: User)

    fun searchUser(email: String, password: String): User?

}