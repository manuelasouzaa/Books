package com.example.projeto.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Book (
    val id: String,
    val title: String,
    val author: String,
    val description: String?,
    val image: String?
): Parcelable