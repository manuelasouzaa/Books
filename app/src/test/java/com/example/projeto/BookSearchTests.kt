package com.example.projeto

import com.example.projeto.json.GoogleApiAnswer
import com.example.projeto.web.WebService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class BookSearchTests {

    private val service = mockk<WebService>()
    private val answer = mockk<GoogleApiAnswer>()
    private val search = "livro"

    @Test
    fun `should get api answer`()  = runTest {
        coEvery {
            service.searchBooks(search)
        } returns answer

        service.searchBooks(search)

        coVerify {
            service.searchBooks(search)
        }
    }

}