package com.example.projeto.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.projeto.database.LibraryDatabase
import com.example.projeto.model.Book
import com.example.projeto.model.SavedBook
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.util.UUID

class BookDetailsViewModel(context: Context): ViewModel() {

    private val dao by lazy {
        LibraryDatabase.instance(context).savedBookDao()
    }

    suspend fun adicionarLivro(livro: Book, emailUsuario: String) {
        val livroParaSalvar =
            SavedBook(
                image = livro.image.toString(),
                author = livro.author,
                idBook = livro.id,
                description = livro.description.toString(),
                userEmail = emailUsuario,
                id = UUID.randomUUID().toString(),
                title = livro.title
            )

        withContext(IO) {
            dao.salvarLivro(livroParaSalvar)
        }
    }

    fun buscarLivroSalvo(livro: Book, emailUsuario: String): SavedBook? {
        return dao.buscarLivroSalvo(livro.id, emailUsuario)
    }
}