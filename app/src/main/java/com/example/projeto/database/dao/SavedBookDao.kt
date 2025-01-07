package com.example.projeto.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.projeto.model.SavedBook

@Dao
interface SavedBookDao {

    @Insert
    fun salvarLivro(book: SavedBook)

    @Query ("SELECT * FROM SavedBook WHERE userEmail = :userEmail")
    fun exibirLivrosSalvos(userEmail: String): List<SavedBook>?

    @Query ("SELECT * FROM SavedBook WHERE idBook = :idBook AND userEmail = :userEmail")
    fun buscarLivroSalvo(idBook: String, userEmail: String): SavedBook?

    @Delete
    fun removerLivroSalvo(savedBook: SavedBook)

}
