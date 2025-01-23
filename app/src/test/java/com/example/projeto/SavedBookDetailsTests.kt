package com.example.projeto

import com.example.projeto.database.dao.SavedBookDao
import com.example.projeto.model.SavedBook
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Test

class SavedBookDetailsTests {
    private val dao = mockk<SavedBookDao>()

    private val savedBook = SavedBook(
        image = null,
        author = "Autora do livro",
        id = "id",
        idBook = "idDoLivro",
        userEmail = "teste@gmail.com",
        description = "Descrição do livro salvo",
        title = "Título do livro"
    )

    @Test
    fun `should call dao function when removing a book`() {
        coEvery {
            dao.removeSavedBook(savedBook)
        } returns Unit

        dao.removeSavedBook(savedBook)

        coVerify {
            dao.removeSavedBook(savedBook)
        }
    }
}