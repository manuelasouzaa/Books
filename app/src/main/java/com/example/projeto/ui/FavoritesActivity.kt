package com.example.projeto.ui

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.projeto.contextExpresions.userEmail
import com.example.projeto.database.LibraryDatabase
import com.example.projeto.databinding.FavoritesActivityBinding
import com.example.projeto.ui.adapter.FavoritesAdapter
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class FavoritesActivity: UserActivity() {

    private val binding by lazy {
        FavoritesActivityBinding.inflate(layoutInflater)
    }

    private val dao by lazy {
        LibraryDatabase.instance(this).savedBookDao()
    }

    private val emailUser = user.value?.email

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.recycler.adapter = FavoritesAdapter(this)

        lifecycleScope.launch(IO) {

            dao.showSavedBooks(emailUser.toString())
        }

    }
}