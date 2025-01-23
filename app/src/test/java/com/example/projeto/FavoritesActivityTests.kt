package com.example.projeto

import com.example.projeto.database.dao.SavedBookDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Test

class FavoritesActivityTests {

    private val dao = mockk<SavedBookDao>()
    private val userEmail = "teste@gmail.com"

    @Test
    fun `should return a list of savedBooks`() {
        coEvery {
            dao.showSavedBooks(userEmail)
        } returns listOf()

        dao.showSavedBooks(userEmail)

        coVerify {
            dao.showSavedBooks(userEmail)
        }
    }
}