package com.example.projeto.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import com.example.projeto.viewModel.BookDetailsViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookDetailsActivity : UserActivity() {

    private val viewModel: BookDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = BookDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val emailUsuario: String = intent.getStringExtra(usuarioEmail).toString()
        val livro: Book = intent.getParcelableExtra<Book>(idLivro) as Book

        bookDetailsConfig(binding, livro, emailUsuario)

        binding.btnVoltar.setOnClickListener {
            finish()
        }

        binding.btnAdd.setOnClickListener {
            lifecycleScope.launch(IO) {
                val livroSalvo =
                    viewModel.buscarLivroSalvo(livro, emailUsuario, this@BookDetailsActivity)

                if (livroSalvo == null) {
                    adicionarLivro(viewModel, livro, emailUsuario, binding)
                }

                if (livroSalvo !== null) {
                    withContext(Main) {
                        toast("Este livro já foi adicionado")
                    }
                }
            }
        }
    }

    private suspend fun adicionarLivro(
        viewModel: BookDetailsViewModel,
        livro: Book,
        emailUsuario: String,
        binding: BookDetailsBinding
    ) {
        viewModel.adicionarLivro(livro, emailUsuario, this@BookDetailsActivity)
        withContext(Main) {
            binding.btnAdd.setImageResource(R.drawable.ic_bookmark_added)
            exibirCaixaDialogo(emailUsuario)
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
        livro: Book,
        emailUsuario: String
    ) {
        binding.livroTitulo.text = livro.title
        binding.livroDesc.text = livro.description
        binding.livroImagem.loadImage(livro.image)

        when {
            livro.author == "null" -> {
                binding.livroAutor.text = ""
            }

            livro.author != "null" -> {
                binding.livroAutor.text = livro.author.toString()
            }
        }

        lifecycleScope.launch(IO) {
            val livroSalvo = viewModel.buscarLivroSalvo(livro, emailUsuario, this@BookDetailsActivity)

            if (livroSalvo !== null)
                binding.btnAdd.setImageResource(R.drawable.ic_bookmark_added)
        }
    }
}

