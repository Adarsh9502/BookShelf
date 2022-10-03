package com.example.bookshelf

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.bookshelf.adapters.BookSearchAdapter
import com.example.bookshelf.dao.AddToShelfDao
import com.example.bookshelf.databinding.FragmentSearchBinding
import com.example.bookshelf.model.api.VolumeInfo
import com.example.bookshelf.repository.Repository


class SearchFragment : Fragment(), BookSearchAdapter.AddToShelf {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchViewModel
    private lateinit var addToShelfDao: AddToShelfDao
    private val bookSearchAdapter by lazy { BookSearchAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)

        binding.progressBar.visibility = View.GONE
        setupRecyclerView()

        addToShelfDao = AddToShelfDao()
        Glide.with(requireActivity()).load(addToShelfDao.auth.currentUser?.photoUrl).circleCrop().into(binding.profile)

        binding.profile.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_profileFragment)
        }

        val repository = Repository()
        val viewModelFactory = SearchViewModelFactory(repository)
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory)[SearchViewModel::class.java]

        binding.search.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                // If the event is a key-down event on the "enter" button
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    performSearch()
                    return true
                }
                return false
            }
        })

        binding.searchButton.setOnClickListener {
            performSearch()
        }

        binding.profile.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_profileFragment)
        }

        return binding.root
    }

    private fun performSearch() {
        if (binding.search.text.toString().isEmpty())
            Toast.makeText(requireActivity(), "Search Field Empty", Toast.LENGTH_SHORT).show()
        else {
            binding.progressBar.visibility = View.VISIBLE
            try {
                val imm =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, 0)
            } catch (e: Exception) {
                // TODO: handle exception
            }

            val searchedQuery = binding.search.text.toString()
            //searchedQuery += "+inauthor"

            viewModel.getBooks(searchedQuery, "relevance", 40)
            viewModel.myResponse.observe(viewLifecycleOwner, Observer { response ->
                if (response.isSuccessful) {
                    binding.progressBar.visibility = View.GONE
                    response.body()?.let { it -> bookSearchAdapter.setData(it.items, this) }
                } else {
                    Log.d("Books", response.code().toString())
                }
            })
        }
    }

    private fun setupRecyclerView() {
        binding.searchResults.adapter = bookSearchAdapter
        binding.searchResults.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onBookAdded(volumeInfo: VolumeInfo?) {
        addToShelfDao = AddToShelfDao()
        if (volumeInfo != null) {
            addToShelfDao.addToShelf(volumeInfo)
            Toast.makeText(requireActivity(), "Added to BookShelf", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(requireActivity(), "Error Adding to Shelf", Toast.LENGTH_SHORT).show()
        }
    }
}