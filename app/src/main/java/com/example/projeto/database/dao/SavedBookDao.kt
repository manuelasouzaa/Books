package com.example.projeto.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.projeto.model.SavedBook

@Dao
interface SavedBookDao {

    @Insert
    fun saveBook(book: SavedBook)

    @Query ("SELECT * FROM SavedBook WHERE userEmail = :userEmail")
    fun showSavedBooks(userEmail: String): List<SavedBook>
}