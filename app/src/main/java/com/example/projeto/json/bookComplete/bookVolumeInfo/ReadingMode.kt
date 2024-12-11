package com.example.projeto.json.bookComplete.bookVolumeInfo

import com.google.gson.annotations.SerializedName

data class ReadingMode(
    @SerializedName("text")
    val text: Boolean?,

    @SerializedName("image")
    val image: Boolean?
)
