package com.example.projeto.json.bookComplete.accessInfo

import com.google.gson.annotations.SerializedName

data class FileType(

    @SerializedName("isAvailable")
    val isAvailable: Boolean?,

    @SerializedName("acsTokenLink")
    val acsTokenLink: String?
)
