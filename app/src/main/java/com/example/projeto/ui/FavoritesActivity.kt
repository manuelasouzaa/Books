package com.example.projeto.ui

import android.os.Bundle
import android.view.View.VISIBLE
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.projeto.contextExpresions.userEmail
import com.example.projeto.database.LibraryDatabase
import com.example.projeto.databinding.FavoritesActivityBinding
import com.example.projeto.ui.adapter.FavoritesAdapter
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesActivity : UserActivity() {

    private val binding by lazy {
        FavoritesActivityBinding.inflate(layoutInflater)
    }

    private val dao by lazy {
        LibraryDatabase.instance(this).savedBookDao()
    }

    private val adapter = FavoritesAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val emailUser = intent.getStringExtra(userEmail).toString()

        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch(IO) {
            val savedBooks = dao.showSavedBooks(emailUser)
            if (savedBooks != null) {
                adapter.update(savedBooks)
            }
            if (savedBooks == null) {
                withContext(Main) {
                    binding.noBooks.visibility = VISIBLE
                }
            }
        }

    }
}