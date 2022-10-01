package com.example.bookshelf

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.bookshelf.dao.AddToShelfDao
import com.example.bookshelf.databinding.FragmentBookDetailsBinding
import com.example.bookshelf.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

class BookDetailsFragment : Fragment() {

    private var _binding: FragmentBookDetailsBinding? = null
    private val binding get() = _binding!!
    val args: BookDetailsFragmentArgs by navArgs()

    private lateinit var addToShelfDao: AddToShelfDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookDetailsBinding.inflate(inflater, container, false)

        addToShelfDao = AddToShelfDao()
        Glide.with(requireActivity()).load(addToShelfDao.auth.currentUser?.photoUrl).circleCrop()
            .into(binding.profile)

        val bookName = args.bookName
        val bookAuthor = args.bookAuthor
        val image = args.imageLink
        val description = args.description
        val infoLink = args.infoLink
        val docId = args.docId

        binding.bookName.text = bookName
        binding.description.text = description
        binding.bookAuthor.text = bookAuthor
        Glide.with(requireActivity()).load(image).fitCenter().into(binding.bookImage)

        binding.profile.setOnClickListener {
            findNavController().navigate(R.id.action_bookDetailsFragment_to_profileFragment)
        }

        binding.share.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, infoLink)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)

        }

        binding.remove.setOnClickListener {
            addToShelfDao.bookCollections.document(docId).delete()
                .addOnSuccessListener {
                    Toast.makeText(
                        requireActivity(),
                        "Book deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        requireActivity(),
                        "Error deleting this book",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}