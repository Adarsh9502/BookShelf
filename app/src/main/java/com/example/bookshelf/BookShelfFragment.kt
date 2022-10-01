package com.example.bookshelf

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.bookshelf.adapters.BookShelfAdapter
import com.example.bookshelf.dao.AddToShelfDao
import com.example.bookshelf.databinding.FragmentBookShelfBinding
import com.example.bookshelf.model.api.VolumeInfo
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BookShelfFragment : Fragment() {

    private var _binding: FragmentBookShelfBinding? = null
    private val binding get() = _binding!!
    private lateinit var addToShelfDao: AddToShelfDao
    private lateinit var bookShelfAdapter: BookShelfAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookShelfBinding.inflate(inflater, container, false)

        addToShelfDao = AddToShelfDao()
        Glide.with(requireActivity()).load(addToShelfDao.auth.currentUser?.photoUrl).circleCrop()
            .into(binding.profile)
        setupRecyclerView()

        binding.profile.setOnClickListener {
            findNavController().navigate(R.id.action_bookShelfFragment_to_profileFragment)
        }

        binding.searchButton.setOnClickListener {
            findNavController().navigate(R.id.action_bookShelfFragment_to_searchFragment)
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        addToShelfDao = AddToShelfDao()
        val bookCollections = addToShelfDao.bookCollections
        val query = bookCollections.orderBy("title")
        val recyclerOptions =
            FirestoreRecyclerOptions.Builder<VolumeInfo>()
                .setQuery(query, VolumeInfo::class.java)
                .build()

        bookShelfAdapter = BookShelfAdapter(recyclerOptions)
        binding.bookshelf.adapter = bookShelfAdapter
        val horizontalLayout =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.bookshelf.layoutManager = horizontalLayout
        
    }

    override fun onStart() {
        super.onStart()
        bookShelfAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        bookShelfAdapter.stopListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onBookOpen(id: String) {
//        addToShelfDao = AddToShelfDao()
//        val bookDetails = addToShelfDao.getBookById(id).result.toObject(VolumeInfo::class.java)
//        val bookName = bookDetails?.title.toString()
//        val authorList: List<String>? = bookDetails?.authors
//        var author = ""
//        if (authorList != null) {
//            when (authorList.size) {
//                1 -> author = authorList[0]
//                else -> {
//                    for (i in authorList.indices)
//                        author += authorList[i] + ", "
//                }
//            }
//        }
//        val description = bookDetails?.description.toString()
//        val infoLink = bookDetails?.infoLink.toString()
//        val thumbnail = bookDetails?.imageLinks?.thumbnail.toString()
//        val action = BookShelfFragmentDirections.actionBookShelfFragmentToBookDetailsFragment(
//            bookName,
//            author,
//            description,
//            infoLink,
//            thumbnail,
//            id
//        )
//        Navigation.findNavController(binding.root).navigate(action)
//    }

}