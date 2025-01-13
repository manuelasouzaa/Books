package com.example.projeto.web


import com.example.projeto.json.GoogleApiAnswer
import retrofit2.http.GET
import retrofit2.http.Query

interface WebService {

    @GET("books/v1/volumes")
    suspend fun searchBooks(
        @Query("q") pesquisa: String): GoogleApiAnswer

}
