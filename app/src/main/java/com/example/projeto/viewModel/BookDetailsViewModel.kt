package com.example.projeto.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.projeto.database.LibraryDatabase
import com.example.projeto.model.Book
import com.example.projeto.model.SavedBook
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.util.UUID

class BookDetailsViewModel: ViewModel() {

    suspend fun adicionarLivro(livro: Book, emailUsuario: String, context: Context) {
        val livroParaSalvar =
            SavedBook(
                image = livro.image.toString(),
                author = livro.author,
                idBook = livro.id,
                description = livro.description.toString(),
                userEmail = emailUsuario,
                id = UUID.randomUUID().toString(),
                title = livro.title.toString()
            )

        val dao = LibraryDatabase.instance(context).savedBookDao()
        withContext(IO) {
            dao.salvarLivro(livroParaSalvar)
        }
    }

    fun buscarLivroSalvo(livro: Book, emailUsuario: String, context: Context): SavedBook? {
        val dao = LibraryDatabase.instance(context).savedBookDao()
        return dao.buscarLivroSalvo(livro.id, emailUsuario)
    }
}