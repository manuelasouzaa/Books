package com.example.projeto.json.bookComplete.bookVolumeInfo

import com.google.gson.annotations.SerializedName

data class IndustryIdentifier(

    @SerializedName("type")
    val type: String?,

    @SerializedName("identifier")
    val identifier: String?
)
