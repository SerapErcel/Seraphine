package com.serapercel.seraphine.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.serapercel.seraphine.adapter.CategoryAdapter
import com.serapercel.seraphine.databinding.FragmentHomeBinding
import com.serapercel.seraphine.model.MusicCategory
import com.serapercel.seraphine.util.toastLong

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        getMusicCategories().addOnSuccessListener { musicList ->
            val recyclerView: RecyclerView = binding.rvMain
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = CategoryAdapter(musicList)

        }.addOnFailureListener { e ->
            requireContext().toastLong(e.message.toString())
        }
        return binding.root
    }

    private fun getMusicCategories(): Task<MutableList<MusicCategory>> {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val collectionRef =
            db.collection("users").document(auth.currentUser!!.uid).collection("musics")

        val newList = mutableListOf<MusicCategory>()

        return collectionRef.get()
            .continueWith { task ->
                val querySnapshot = task.result
                for (document in querySnapshot.documents) {
                    val data = document.toObject(MusicCategory::class.java)
                    data?.let {
                        newList.add(it)
                    }
                }
                newList
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}