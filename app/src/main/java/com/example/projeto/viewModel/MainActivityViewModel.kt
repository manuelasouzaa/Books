package com.example.projeto.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.projeto.contextExpresions.irPara
import com.example.projeto.contextExpresions.usuarioEmail
import com.example.projeto.databinding.ActivityMainBinding
import com.example.projeto.json.GoogleApiAnswer
import com.example.projeto.model.Book
import com.example.projeto.model.User
import com.example.projeto.ui.SearchActivity
import com.example.projeto.web.Retrofit
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable

class MainActivityViewModel : ViewModel() {

    private val service by lazy {
        Retrofit().webService
    }

    suspend fun pesquisarLivro(busca: String, context: Context, usuario: StateFlow<User?>, binding: ActivityMainBinding) {
        withContext(IO) {
            launch {
                val lista = service.buscarLivros(busca)
                buscarLivro(lista, context, usuario, binding)
            }
        }
    }

    private suspend fun buscarLivro(
        list: GoogleApiAnswer,
        context: Context,
        usuario: StateFlow<User?>,
        binding: ActivityMainBinding
    ) {
        try {
            val booklist: List<Book?> = obterLista(list)
            enviarLista(booklist, context, usuario)
        } catch (e: Exception) {
            withContext(Main) {
                launch {
                    Toast.makeText(context, "Livro não encontrado", Toast.LENGTH_SHORT).show()
                    binding.editText.text.clear()
                }
            }
        }
    }

    private fun obterLista(list: GoogleApiAnswer): List<Book?> {
        return list.items.map { item ->
            item.volumeInfo?.let {
                val book = Book(
                    id = item.id,
                    title = it.title,
                    author = it.authors.toString(),
                    description = it.description?.toString(),
                    image = it.imageLinks?.thumbnail
                )
                book
            }
        }
    }

    private suspend fun enviarLista(
        booklist: List<Book?>,
        context: Context,
        usuario: StateFlow<User?>
    ) {
        withContext(IO) {
            usuario.filterNotNull().collect {
                val email = usuario.value?.email.toString()

                withContext(Main) {
                    context.irPara(SearchActivity::class.java) {
                        putExtra("booklist", booklist as Serializable)
                        putExtra(usuarioEmail, email)
                    }
                }
            }
        }
    }
}