package com.example.projeto.ui

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.projeto.R
import com.example.projeto.contextExpresions.irPara
import com.example.projeto.contextExpresions.toast
import com.example.projeto.contextExpresions.usuarioEmail
import com.example.projeto.contextExpresions.usuarioLogado
import com.example.projeto.databinding.ActivityMainBinding
import com.example.projeto.viewModel.MainActivityViewModel
import kotlinx.coroutines.Delay
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        Log.i("TAG", "onCreate: teste")

        binding.btnBuscar.setOnClickListener {
            try {
                lifecycleScope.launch(IO) {
                    viewModel.pesquisarLivro(
                        binding.editText.text.toString(),
                        this@MainActivity,
                        usuario,
                        binding
                    )
                }
                binding.loading.visibility = VISIBLE
                binding.btnBuscar.visibility = GONE
                binding.editText.text.clear()
            } catch (e: Exception) {
                Log.e("erro", "Erro ao pesquisar", e)
                binding.editText.text.clear()
                toast("Erro ao pesquisar")
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