package com.example.projeto.json.bookComplete.bookVolumeInfo

import com.google.gson.annotations.SerializedName

data class PanelizationSummary(

    @SerializedName("containsEpubBubbles")
    val containsEpubBubbles: Boolean?,

    @SerializedName("containsImagesBubbles")
    val containsImageBubbles: Boolean?
)
