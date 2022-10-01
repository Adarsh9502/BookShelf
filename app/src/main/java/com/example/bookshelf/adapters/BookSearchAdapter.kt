package com.example.bookshelf.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookshelf.R
import com.example.bookshelf.model.api.Items
import com.example.bookshelf.model.api.VolumeInfo

class BookSearchAdapter : RecyclerView.Adapter<BookSearchAdapter.ViewHolder>() {

    private var booksList = emptyList<Items>()
    private lateinit var listener: AddToShelf

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var bookName: TextView = view.findViewById(R.id.bookName)
        var bookAuthor: TextView = view.findViewById(R.id.bookAuthor)
        var thumbnail: ImageView = view.findViewById(R.id.thumbnail)
        var addButton: ImageButton = view.findViewById(R.id.addToShelf)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_layout, parent, false)
        val viewHolder = ViewHolder(view)
        viewHolder.addButton.setOnClickListener {
            listener.onBookAdded(booksList[viewHolder.adapterPosition].volumeInfo)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bookName.text = booksList[position].volumeInfo.title
        val authorList: List<String>? = booksList[position].volumeInfo.authors
        var authors = ""
        if (authorList != null) {
            when (authorList.size) {
                1 -> authors = authorList[0]
                else -> {
                    for (i in authorList.indices)
                        authors += authorList[i] + ", "
                }
            }
        }
        holder.bookAuthor.text = authors
        Glide.with(holder.thumbnail.context)
            .load(booksList[position].volumeInfo.imageLinks?.thumbnail)
            .centerCrop()
            .into(holder.thumbnail)
    }

    override fun getItemCount(): Int {
        return booksList.size
    }

    fun setData(newList: List<Items>?, clickListener: AddToShelf) {
        if (newList != null) {
            booksList = newList
            listener = clickListener
            notifyDataSetChanged()
        }
    }

    interface AddToShelf {
        fun onBookAdded(volumeInfo: VolumeInfo?)
    }
}
