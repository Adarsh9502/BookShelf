package com.example.bookshelf.model.api

data class Books (
    val kind: String,
    val items: List<Items>,
    val totalItems: Int
        )