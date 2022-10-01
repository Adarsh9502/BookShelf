package com.example.bookshelf.api

import com.example.bookshelf.model.api.Books
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SimpleApi {

    @GET("v1/volumes")
    suspend fun getBooks(
        @Query(value = "q") searchQuery: String,
        @Query(value = "orderBy") order: String
    ): Response<Books>

}