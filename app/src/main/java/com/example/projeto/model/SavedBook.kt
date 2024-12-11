package com.example.projeto.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class SavedBook (

    @ColumnInfo(name = "image")
    val image: String?,
    @ColumnInfo(name = "author")
    val author: String?,
    @ColumnInfo(name = "idBook")
    val idBook: String,
    @ColumnInfo(name = "description")
    val description: String?,
    @ColumnInfo(name = "userEmail")
    val userEmail: String,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "title")
    val title: String,



): Parcelable