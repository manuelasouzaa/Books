package com.example.projeto

import com.example.projeto.database.dao.SavedBookDao
import com.example.projeto.model.SavedBook
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Test

class BookDetailsTests {
    val dao = mockk<SavedBookDao>()

    @Test
    fun `call dao function when saving a book`() {
        val bookToSave = SavedBook(
            image = null,
            author = "autor",
            id = "aaaaa",
            description = "descrição",
            userEmail = "teste@gmail.com",
            idBook = "eeeee",
            title = "titulo"
        )

        coEvery {
            dao.saveBook(bookToSave)
        }.returns(Unit)

        dao.saveBook(bookToSave)

        coVerify {
            dao.saveBook(bookToSave)
        }
    }
}