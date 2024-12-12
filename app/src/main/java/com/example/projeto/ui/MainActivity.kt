package com.example.projeto.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.projeto.contextExpresions.goTo
import com.example.projeto.contextExpresions.loggedUser
import com.example.projeto.contextExpresions.toast
import com.example.projeto.contextExpresions.userEmail
import com.example.projeto.databinding.ActivityMainBinding
import com.example.projeto.json.GoogleApiAnswer
import com.example.projeto.model.Book
import com.example.projeto.web.Retrofit
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable

class MainActivity : UserActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val service by lazy {
        Retrofit().webService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.btnAccount.setOnClickListener {
            goTo(AccountActivity::class.java) {
                loggedUser
            }
        }

        binding.btnFavorites.setOnClickListener {
            lifecycleScope.launch(IO) {
                user.filterNotNull().collect {
                    val email = user.value?.email.toString()
                    goTo(FavoritesActivity::class.java) {
                        putExtra(userEmail, email)
                        loggedUser
                    }
                }
            }
        }


        Log.i("preferences", "onCreate: $loggedUser")
        Log.i("usuario", "onCreate: ${user.value?.toString()}")

        binding.search.setOnClickListener {
            lifecycleScope.launch(IO) {
                launch {
                    try {
                        val list = service.searchBooks(binding.editText.text.toString())
                        search(list)
                    } catch (e: Exception) {
                        Log.e("erro", "pesquisa não realizada", e)
                        withContext(Main) {
                            toast("Livro não encontrado")
                        }
                    }
                }
            }
        }
    }

    private fun search(list: GoogleApiAnswer) {
        lifecycleScope.launch(Main) {
            val booklist: List<Book?> = getList(list)
            Log.i("listaLivros", "search: $booklist")

            sendList(booklist)
        }
    }

    private fun getList(list: GoogleApiAnswer): List<Book?> {
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

    private fun sendList(booklist: List<Book?>) {
        lifecycleScope.launch(IO) {
            user.filterNotNull().collect {
                val email = user.value?.email.toString()
                Log.i("email usuario", "onCreate: $email")

                withContext(Main) {
                    goTo(SearchActivity::class.java) {
                        putExtra("booklist", booklist as Serializable)
                        putExtra(userEmail, email)
                    }
                }
            }
        }
    }
}