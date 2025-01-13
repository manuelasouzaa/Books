package com.example.projeto.ui

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.projeto.contextExpresions.irPara
import com.example.projeto.contextExpresions.toast
import com.example.projeto.contextExpresions.usuarioEmail
import com.example.projeto.contextExpresions.usuarioLogado
import com.example.projeto.databinding.ActivityMainBinding
import com.example.projeto.viewModel.MainActivityViewModel
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

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        val email = usuario.value?.email.toString()

        binding.btnBuscar.setOnClickListener {
            binding.loading.visibility = VISIBLE
            binding.btnBuscar.visibility = GONE

            val busca = binding.editText.text.toString()


            try {
                lifecycleScope.launch {
                    viewModel.searchBook(busca)

                    viewModel.message.collect {
                        if (it == "Livro não encontrado")
                            erroAoPesquisar()

                        if (it == "Livros encontrados") {
                            viewModel.booklist.collect {
                                irPara(SearchActivity::class.java) {
                                    putExtra("booklist", it as Serializable)
                                    putExtra(usuarioEmail, email)
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("erro", "Erro ao pesquisar", e)
                erroAoPesquisar()
            }
            binding.editText.text.clear()
        }

        binding.btnConta.setOnClickListener {
            irParaActivity(AccountActivity::class.java, email)
            finish()
        }

        binding.btnBooklist.setOnClickListener {
            irParaActivity(FavoritesActivity::class.java, email)
        }
    }

    private fun erroAoPesquisar() {
        toast("Erro ao pesquisar")
        binding.loading.visibility = GONE
        binding.btnBuscar.visibility = VISIBLE
    }

    private fun irParaActivity(activity: Class<*>, email: String) {
        irPara(activity) {
            putExtra(usuarioEmail, email)
            usuarioLogado
        }
    }
}