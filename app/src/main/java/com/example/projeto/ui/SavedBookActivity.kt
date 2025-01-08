package com.example.projeto.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import androidx.lifecycle.lifecycleScope
import com.example.projeto.contextExpresions.idLivro
import com.example.projeto.contextExpresions.irPara
import com.example.projeto.contextExpresions.loadImage
import com.example.projeto.contextExpresions.usuarioEmail
import com.example.projeto.contextExpresions.usuarioLogado
import com.example.projeto.database.LibraryDatabase
import com.example.projeto.databinding.DeleteBookDialogBinding
import com.example.projeto.databinding.SavedBookActivityBinding
import com.example.projeto.model.SavedBook
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class SavedBookActivity : UserActivity() {

    private val binding by lazy {
        SavedBookActivityBinding.inflate(layoutInflater)
    }

    private val dao by lazy {
        LibraryDatabase.instance(this).savedBookDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val livroFavorito = intent.getParcelableExtra<SavedBook>(idLivro) as SavedBook
        val emailUsuario = intent.getStringExtra(usuarioEmail)

        savedBookConfig(livroFavorito)

        binding.btnVoltar.setOnClickListener {
            irParaBooklist(emailUsuario)
        }

        binding.btnRemover.setOnClickListener {
            exibirCaixaDialogo(livroFavorito, emailUsuario)
        }
    }

    private fun savedBookConfig(livroFavorito: SavedBook) {
        binding.livroTitulo.text = livroFavorito.title
        binding.livroImagem.loadImage(livroFavorito.image)

        if (livroFavorito.author.isNullOrEmpty())
            binding.livroAutor.text = ""
        if (livroFavorito.author == "null")
            binding.livroAutor.text = ""
        if (livroFavorito.author != "null")
            binding.livroAutor.text = livroFavorito.author

        if (livroFavorito.description.isNullOrEmpty())
            binding.livroDesc.text = ""
        if (livroFavorito.description == "null")
            binding.livroDesc.text = ""
        if (livroFavorito.description != "null")
            binding.livroDesc.text = livroFavorito.description
    }

    private fun irParaBooklist(emailUsuario: String?) {
        irPara(FavoritesActivity::class.java) {
            putExtra(usuarioEmail, emailUsuario)
            usuarioLogado
        }
        finish()
    }

    private fun exibirCaixaDialogo(livroSalvo: SavedBook, emailUsuario: String?) {
        val dialog = Dialog(this)
        val bindingDialog = DeleteBookDialogBinding.inflate(layoutInflater)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(bindingDialog.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btnSim = bindingDialog.btnSim
        val btnNao = bindingDialog.btnNao

        btnNao.setOnClickListener {
            dialog.dismiss()
        }

        btnSim.setOnClickListener {
            dialog.dismiss()
            removerLivro(livroSalvo, emailUsuario)
        }
        dialog.show()
    }

    private fun removerLivro(
        livroSalvo: SavedBook,
        emailUsuario: String?
    ) {
        lifecycleScope.launch(IO) {
            dao.removerLivroSalvo(livroSalvo)
            finish()
            irParaBooklist(emailUsuario)
        }
    }
}