package com.example.bookshelf.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookshelf.BookShelfFragmentDirections
import com.example.bookshelf.R
import com.example.bookshelf.model.api.VolumeInfo
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import java.util.*


class BookShelfAdapter(options: FirestoreRecyclerOptions<VolumeInfo>) :
    FirestoreRecyclerAdapter<VolumeInfo, BookShelfAdapter.BooksViewHolder>(options) {

    inner class BooksViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookImage: ImageView = view.findViewById(R.id.bookImage)
        val bookName: TextView = view.findViewById(R.id.bookName)
        val bookAuthor: TextView = view.findViewById(R.id.bookAuthor)
        val openButton: ImageButton = view.findViewById(R.id.open)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        return BooksViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.bookshelf_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int, model: VolumeInfo) {
        holder.bookName.text = model.title
        val authorList: List<String>? = model.authors
        var author = ""
        if (authorList != null) {
            when (authorList.size) {
                1 -> author = authorList[0]
                else -> {
                    for (i in authorList.indices)
                        author += authorList[i] + ", "
                }
            }
        }
        val image = model.imageLinks?.large
            ?: (model.imageLinks?.medium
                ?: (model.imageLinks?.small
                    ?: model.imageLinks?.thumbnail))

        holder.bookAuthor.text = author
        Glide.with(holder.bookImage.context)
            .load(image)
            .fitCenter()
            .into(holder.bookImage)

        val id = snapshots.getSnapshot(position).id

        holder.openButton.setOnClickListener {
            val action = BookShelfFragmentDirections.actionBookShelfFragmentToBookDetailsFragment(
                model.title.toString(),
                author, model.description.toString(),
                model.infoLink.toString(),
                image.toString(), id)
            Navigation.createNavigateOnClickListener(action).onClick(holder.itemView)
        }
    }

}