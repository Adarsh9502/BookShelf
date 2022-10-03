package com.example.bookshelf.repository

import com.example.bookshelf.api.RetrofitInstance
import com.example.bookshelf.model.api.Books
import retrofit2.Response

class Repository {

    suspend fun getBooks(search: String, order: String, results: Int): Response<Books> {
        return RetrofitInstance.api.getBooks(search, order, results)
    }

}