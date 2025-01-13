package com.example.projeto.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projeto.json.GoogleApiAnswer
import com.example.projeto.model.Book
import com.example.projeto.web.Retrofit
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {

    private val service by lazy {
        Retrofit().webService
    }

    private val _state: MutableStateFlow<State> = MutableStateFlow(State.Loading)
    val state: StateFlow<State> = _state.asStateFlow()

    private val _message: MutableSharedFlow<String> = MutableSharedFlow(0)
    val message: SharedFlow<String> = _message.asSharedFlow()

    private val _booklist: MutableSharedFlow<List<Book?>> = MutableSharedFlow(0)
    val booklist: SharedFlow<List<Book?>> = _booklist.asSharedFlow()

    fun searchBook(search: String) {
        viewModelScope.launch(IO) {
            _state.emit(State.Loading)
            val list = service.searchBooks(search)
            fetchBook(list)
        }
    }

    private suspend fun fetchBook(list: GoogleApiAnswer) {
        val booklist = verifyAnswer(list)

        if (booklist.isNullOrEmpty())
            errorInSearch()

        viewModelScope.launch {
            booklist?.map {
                when {
                    it?.title?.isNotBlank() == true ->
                        sendList(booklist)

                    it?.title.isNullOrEmpty() ->
                        errorInSearch()

                    else -> errorInSearch()
                }
            }
        }
    }

    private fun verifyAnswer(lista: GoogleApiAnswer): List<Book?>? {
        return when {
            lista.totalItems == 0 ->
                null

            lista.totalItems > 0 ->
                getList(lista)

            else ->
                null
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

    private suspend fun sendList(booklist: List<Book?>) {
        _state.emit(State.Loaded)
        _booklist.emit(booklist)
        _message.emit("Livros encontrados")
    }

    private suspend fun errorInSearch() {
        _state.emit(State.Error)
        _message.emit("Livro não encontrado")
        return
    }

    sealed class State {
        object Error : State()
        object Loaded : State()
        object Loading : State()
    }
}