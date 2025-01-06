package com.example.projeto.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.contextExpresions.userEmail
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

    suspend fun searchApi(search: String, context: Context, user: StateFlow<User?>) {
        try {
            val list = service.searchBooks(search)
            withContext(IO) {
                search(list, context, user)
            }
        } catch (e: Exception) {
            Log.e("erro", "pesquisa não realizada", e)
            withContext(Main) {
                launch {
                    Toast.makeText(
                        context,
                        "Pesquisa não realizada",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    suspend fun search(list: GoogleApiAnswer, context: Context, user: StateFlow<User?>) {
        withContext(Main) {
            val booklist: List<Book?> = getList(list)
            sendList(booklist, context, user)
        }
    }

    fun getList(list: GoogleApiAnswer): List<Book?> {
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

    suspend fun sendList(booklist: List<Book?>, context: Context, user: StateFlow<User?>) {
        withContext(IO) {
            user.filterNotNull().collect {
                val email = user.value?.email.toString()

                withContext(Main) {
                    context.goTo(SearchActivity::class.java) {
                        putExtra("booklist", booklist as Serializable)
                        putExtra(userEmail, email)
                    }
                }
            }
        }
    }
}