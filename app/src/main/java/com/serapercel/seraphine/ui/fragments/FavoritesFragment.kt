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
import com.serapercel.seraphine.adapter.FavoriteAdapter
import com.serapercel.seraphine.databinding.FragmentFavoritesBinding
import com.serapercel.seraphine.model.Music
import com.serapercel.seraphine.util.toastLong

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        getMusicCategories().addOnSuccessListener { musicList ->
            val recyclerView: RecyclerView = binding.rvFavorites
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.adapter = FavoriteAdapter(musicList)

        }.addOnFailureListener { e ->
            requireContext().toastLong(e.message.toString())
        }

        return binding.root
    }

    private fun getMusicCategories(): Task<MutableList<Music>> {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val collectionRef =
            db.collection("users").document(auth.currentUser!!.uid).collection("favorites")

        val newList = mutableListOf<Music>()

        return collectionRef.get()
            .continueWith { task ->
                val querySnapshot = task.result
                for (document in querySnapshot.documents) {
                    val data = document.toObject(Music::class.java)
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