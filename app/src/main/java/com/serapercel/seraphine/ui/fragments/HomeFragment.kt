package com.serapercel.seraphine.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.serapercel.seraphine.databinding.FragmentHomeBinding
import com.serapercel.seraphine.model.MusicCategory
import com.serapercel.seraphine.util.toastLong


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var list: ArrayList<MusicCategory>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readFirebase()

    }

    private fun readFirebase() {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        val collectionRef =
            db.collection("users").document(auth.currentUser!!.uid).collection("musics")
        collectionRef.get()
            .addOnSuccessListener { querySnapshot ->
                Log.d("music", "querySnapshot.isEmpty " + querySnapshot.isEmpty.toString())

                for (document in querySnapshot.documents) {
                    Log.d("music", document.data.toString())
                    val data = document.toObject(MusicCategory::class.java)
                    list.add(data!!)
                    Log.d("music", "Okunan veri: $data")
                }
            }
            .addOnFailureListener {
                requireContext().toastLong(it.message.toString())
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}