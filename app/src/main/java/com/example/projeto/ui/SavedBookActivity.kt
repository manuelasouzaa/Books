package com.example.projeto.ui

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.projeto.contextExpresions.idLivro
import com.example.projeto.contextExpresions.irPara
import com.example.projeto.contextExpresions.loadImage
import com.example.projeto.contextExpresions.usuarioLogado
import com.example.projeto.contextExpresions.toast
import com.example.projeto.contextExpresions.usuarioEmail
import com.example.projeto.database.LibraryDatabase
import com.example.projeto.databinding.SavedBookActivityBinding
import com.example.projeto.model.SavedBook
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        binding.btnVoltar.setOnClickListener {
            irParaBooklist(emailUsuario)
        }

        binding.livroTitulo.text = livroFavorito.title
        binding.livroDesc.text = livroFavorito.description.toString()
        binding.livroImagem.loadImage(livroFavorito.image)

        if (livroFavorito.author == "null")
            binding.livroAutor.text = ""
        if (livroFavorito.author !== "null")
            binding.livroAutor.text = livroFavorito.author.toString()

        binding.btnRemover.setOnClickListener {
            removerDaBooklist(livroFavorito, emailUsuario)
        }
    }

    private fun removerDaBooklist(
        livroSalvo: SavedBook,
        emailUsuario: String?
    ) {
        lifecycleScope.launch(IO) {
            dao.removerLivroSalvo(livroSalvo)
            withContext(Main) {
                toast("Livro excluído com sucesso")
                irParaBooklist(emailUsuario)
            }
        }
    }

    private fun irParaBooklist(emailUsuario: String?) {
        irPara(FavoritesActivity::class.java) {
            putExtra(usuarioEmail, emailUsuario)
            usuarioLogado
        }
        finish()
    }
}