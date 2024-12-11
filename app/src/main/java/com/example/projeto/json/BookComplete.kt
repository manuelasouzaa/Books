package com.example.projeto.json

import com.example.projeto.json.bookComplete.AccessInfo
import com.example.projeto.json.bookComplete.BookVolumeInfo
import com.example.projeto.json.bookComplete.SaleInfo
import com.example.projeto.json.bookComplete.SearchInfo
import com.google.gson.annotations.SerializedName

data class BookComplete(
    @SerializedName("kind")
    val kind: String,

    @SerializedName("id")
    val id: String,

    @SerializedName("etag")
    val etag: String,

    @SerializedName("selfLink")
    val selfLink: String?,

    @SerializedName("volumeInfo")
    val volumeInfo: BookVolumeInfo?,

    @SerializedName("saleInfo")
    val saleInfo: SaleInfo?,

    @SerializedName("accessInfo")
    val accessInfo: AccessInfo?,

    @SerializedName("searchInfo")
    val searchInfo: SearchInfo?
)
