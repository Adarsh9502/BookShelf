package com.example.bookshelf

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.bookshelf.dao.AddToShelfDao
import com.example.bookshelf.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var addToShelfDao: AddToShelfDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        addToShelfDao = AddToShelfDao()
        val currentUser = addToShelfDao.auth.currentUser

        binding.profileName.text = currentUser?.displayName
        binding.profileEmail.text = currentUser?.email

        Glide.with(requireActivity()).load(currentUser?.photoUrl).into(binding.profileImage)

        binding.signOut.setOnClickListener {
            addToShelfDao.auth.signOut()
            requireActivity().finishAffinity()
            //findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}