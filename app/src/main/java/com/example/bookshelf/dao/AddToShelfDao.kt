package com.example.bookshelf.dao

import com.example.bookshelf.model.api.VolumeInfo
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

class AddToShelfDao {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val bookCollections = db.collection(auth.uid.toString())

    fun addToShelf(volumeInfo: VolumeInfo) {
        val book = VolumeInfo(
            volumeInfo.title,
            volumeInfo.authors,
            volumeInfo.description,
            volumeInfo.infoLink,
            volumeInfo.imageLinks
        )
        bookCollections.document().set(book)
    }

//    fun getBookById(id: String): Task<DocumentSnapshot> {
//        return bookCollections.document(id).get()
//    }

}