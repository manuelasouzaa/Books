package com.example.projeto.json.bookComplete

import com.example.projeto.json.bookComplete.accessInfo.FileType
import com.google.gson.annotations.SerializedName

data class AccessInfo(
    @SerializedName("country")
    val country: String?,

    @SerializedName("viewability")
    val viewability: String?,

    @SerializedName("embeddabble")
    val embeddable: Boolean?,

    @SerializedName("publicDomain")
    val publicDomain: Boolean?,

    @SerializedName("textToSpeechPermission")
    val textToSpeechPermission: String?,

    @SerializedName("epub")
    val epub: FileType?,

    @SerializedName("pdf")
    val pdf: FileType?,

    @SerializedName("webReaderLink")
    val webReaderLink: String?,

    @SerializedName("accessViewStatus")
    val accessViewStatus: String?,

    @SerializedName("quoteSharingAllowed")
    val quoteSharingAllowed: Boolean?
)
