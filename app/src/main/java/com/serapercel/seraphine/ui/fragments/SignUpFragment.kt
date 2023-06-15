package com.serapercel.seraphine.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.serapercel.seraphine.R
import com.serapercel.seraphine.configs.ApiClient
import com.serapercel.seraphine.databinding.FragmentSignUpBinding
import com.serapercel.seraphine.model.MusicCategory
import com.serapercel.seraphine.model.MusicResponse
import com.serapercel.seraphine.service.MusicService
import com.serapercel.seraphine.util.toastLong
import com.serapercel.seraphine.util.toastShort
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var passwordAgain: String

    lateinit var service: MusicService
    lateinit var list: List<MusicCategory>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.btnSignUpSU.setOnClickListener {
            email = binding.etMailSU.text.toString()
            password = binding.etPasswordSU.text.toString()
            passwordAgain = binding.etPassword2.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && passwordAgain.isNotEmpty()) {
                if (password.length >= 6) {
                    if (password == passwordAgain) {
                        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                            getMusicsFromService()
                            findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
                        }.addOnFailureListener {
                            requireContext().toastLong(it.message.toString())
                        }
                    } else requireContext().toastShort("Passwords are not match!")
                } else requireContext().toastShort("Password is too short!")
            } else requireContext().toastShort("Please fill the fields!")
        }

        binding.tvAlreadyRegister.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    private fun getMusicsFromService() {
        service = ApiClient.getClient().create(MusicService::class.java)

        service.allMusics().enqueue(object : Callback<MusicResponse> {
            override fun onResponse(call: Call<MusicResponse>, response: Response<MusicResponse>) {
                val data = response.body()
                list = data!!.musicCategories!!
                addFirebase()
            }

            override fun onFailure(call: Call<MusicResponse>, t: Throwable) {
                Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun addFirebase() {
        val db = FirebaseFirestore.getInstance()
        val collectionRef =
            db.collection("users").document(auth.currentUser!!.uid).collection("musics")
        for (i in list) {
            collectionRef.add(i)
                .addOnSuccessListener { documentReferences ->
                    // Başarılı bir şekilde eklenmişse burası çalışır.
                    Log.d("music", "Success.")
                }
                .addOnFailureListener { e ->
                    // Bir hata oluşursa burası çalışır.
                    Log.d("music", "Error: $e")
                }
        }
    }
}