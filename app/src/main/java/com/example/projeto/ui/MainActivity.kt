package com.example.projeto.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.projeto.contextExpresions.irPara
import com.example.projeto.contextExpresions.usuarioLogado
import com.example.projeto.contextExpresions.usuarioEmail
import com.example.projeto.databinding.ActivityMainBinding
import com.example.projeto.viewModel.MainActivityViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : UserActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val viewModel: MainActivityViewModel by viewModels()
        binding.btnBuscar.setOnClickListener {
            val busca = binding.editText.text.toString()
            try {
                lifecycleScope.launch(IO) {
                    viewModel.pesquisarLivro(
                        busca,
                        this@MainActivity,
                        usuario
                    )
                }
            } catch (e: Exception) {
                Log.e("erro", "pesquisa não realizada", e)
                Toast.makeText(
                    this@MainActivity,
                    "Pesquisa não realizada",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        binding.btnConta.setOnClickListener {
            irParaActivity(AccountActivity::class.java)
            finish()
        }

        binding.btnBooklist.setOnClickListener {
            irParaActivity(FavoritesActivity::class.java)
        }

    }

    private fun irParaActivity(activity: Class<*>) {
        lifecycleScope.launch(IO) {
            usuario.filterNotNull().collect {
                val email = usuario.value?.email.toString()
                withContext(Main) {
                    irPara(activity) {
                        putExtra(usuarioEmail, email)
                        usuarioLogado
                    }
                }
            }
        }
    }
}