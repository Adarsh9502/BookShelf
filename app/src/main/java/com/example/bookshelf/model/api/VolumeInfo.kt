package com.example.bookshelf.model.api

data class VolumeInfo(
    var title: String? = null,
    var authors: List<String>? = null,
    var description: String? = null,
    var infoLink: String? = null,
    var imageLinks: ImageLinks? = null
)