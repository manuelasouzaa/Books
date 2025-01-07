package com.example.projeto.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.projeto.R
import com.example.projeto.contextExpresions.idLivro
import com.example.projeto.contextExpresions.irPara
import com.example.projeto.contextExpresions.loadImage
import com.example.projeto.contextExpresions.toast
import com.example.projeto.contextExpresions.usuarioEmail
import com.example.projeto.databinding.BookDetailsBinding
import com.example.projeto.databinding.SavedBookDialogBinding
import com.example.projeto.model.Book
import com.example.projeto.model.SavedBook
import com.example.projeto.viewModel.BookDetailsViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookDetailsActivity : UserActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = BookDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val emailUsuario: String = intent.getStringExtra(usuarioEmail).toString()
        val livro: Book = intent.getParcelableExtra<Book>(idLivro) as Book

        val viewModel: BookDetailsViewModel by viewModels()
        val livroSalvo = viewModel.buscarLivroSalvo(livro, emailUsuario)

        bookDetailsConfig(binding, livro, livroSalvo)

        binding.btnVoltar.setOnClickListener {
            finish()
        }

        binding.btnAdd.setOnClickListener {
            val livroSalvo = viewModel.buscarLivroSalvo(livro, emailUsuario)

            if (livroSalvo == null) {
                adicionarLivro(viewModel, livro, emailUsuario, binding)
            }

            if (livroSalvo !== null) {
                toast("Este livro já foi adicionado")
            }
        }
    }

    private fun adicionarLivro(
        viewModel: BookDetailsViewModel,
        livro: Book,
        emailUsuario: String,
        binding: BookDetailsBinding
    ) {
        lifecycleScope.launch(IO) {
            viewModel.adicionarLivro(livro, emailUsuario)
        }
        binding.btnAdd.setImageResource(R.drawable.ic_bookmark_added)
        exibirCaixaDialogo(emailUsuario)
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

