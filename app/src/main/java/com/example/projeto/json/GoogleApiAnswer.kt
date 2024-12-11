package com.example.projeto.json

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GoogleApiAnswer(
    @SerializedName("kind")
    val kind: String,

    @SerializedName("totalItems")
    val totalItems: Int,

    @SerializedName("items")
    val items: List<BookComplete>
): Serializable