package com.example.projeto.web

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retrofit {

    val url_base = "https://www.googleapis.com/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(url_base)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val webService = retrofit.create(WebService::class.java)

}