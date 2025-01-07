package com.example.projeto.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.lifecycle.lifecycleScope
import com.example.projeto.R
import com.example.projeto.contextExpresions.idLivro
import com.example.projeto.contextExpresions.irPara
import com.example.projeto.contextExpresions.loadImage
import com.example.projeto.contextExpresions.toast
import com.example.projeto.contextExpresions.usuarioEmail
import com.example.projeto.database.LibraryDatabase
import com.example.projeto.databinding.BookDetailsBinding
import com.example.projeto.databinding.SavedBookDialogBinding
import com.example.projeto.model.Book
import com.example.projeto.model.SavedBook
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class BookDetailsActivity : UserActivity() {

    private val dao by lazy {
        LibraryDatabase.instance(this).savedBookDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = BookDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val emailUsuario: String = intent.getStringExtra(usuarioEmail).toString()
        val livro: Book = intent.getParcelableExtra<Book>(idLivro) as Book

        lifecycleScope.launch(IO) {
            val livroExistente: SavedBook? = dao.buscarLivroSalvo(livro.id, emailUsuario)
            withContext(Main) {
                bookDetailsConfig(binding, livro, livroExistente)
            }
        }

        binding.btnVoltar.setOnClickListener {
            finish()
        }

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

        val btnAdicionar = binding.btnAdd
        btnAdicionar.setOnClickListener {
            lifecycleScope.launch(IO) {
                if (livroParaSalvar.userEmail == "null")
                    withContext(Main) {
                        toast("Não foi possível salvar o livro")
                    }

                if (livroParaSalvar.userEmail != "null") {
                    val livroExistente = dao.buscarLivroSalvo(livro.id, emailUsuario)
                    when {
                        livroExistente == null -> {
                            dao.salvarLivro(livroParaSalvar)
                            withContext(Main) {
                                binding.btnAdd.setImageResource(R.drawable.ic_bookmark_added)
//                                toast("Adicionado à BookList!")
                                exibirCaixaDialogo(emailUsuario)
                            }
                        }

                        true ->
                            withContext(Main) {
                                toast("Este livro já foi adicionado")
                            }

                    }
                }
            }
        }
    }

    private fun exibirCaixaDialogo(emailUsuario: String) {
        val dialog = Dialog(this)
        val bindingDialog = SavedBookDialogBinding.inflate(layoutInflater)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnVoltarDialog = bindingDialog.btnVoltar
        val btnBooklistDialog = bindingDialog.btnBooklist

        btnVoltarDialog.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        btnBooklistDialog.setOnClickListener {
            dialog.dismiss()
            finish()
            irPara(FavoritesActivity::class.java) {
                putExtra(usuarioEmail, emailUsuario)
            }
        }
        dialog.show()

    }

    private fun bookDetailsConfig(
        binding: BookDetailsBinding,
        book: Book,
        existentBook: SavedBook?
    ) {
        binding.livroTitulo.text = book.title
        binding.livroDesc.text = book.description
        binding.livroImagem.loadImage(book.image)

        when {
            book.author == "null" -> {
                binding.livroAutor.text = ""
            }

            book.author != "null" -> {
                binding.livroAutor.text = book.author.toString()
            }
        }

        if (existentBook !== null)
            binding.btnAdd.setImageResource(R.drawable.ic_bookmark_added)
    }
}

