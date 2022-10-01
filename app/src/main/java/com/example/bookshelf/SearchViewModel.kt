package com.example.bookshelf

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.model.api.Books
import com.example.bookshelf.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class SearchViewModel(private val repository: Repository): ViewModel() {

    val myResponse: MutableLiveData<Response<Books>> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()
    fun getBooks(search: String, order: String) {
        viewModelScope.launch {
            try {
                val response = repository.getBooks(search, order)
                myResponse.value = response
            } catch (e: Exception) {
                //TODO
            }
        }
    }
}