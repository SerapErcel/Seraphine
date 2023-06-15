package com.serapercel.seraphine.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.serapercel.seraphine.R
import com.serapercel.seraphine.databinding.FragmentSignUpBinding
import com.serapercel.seraphine.util.toastLong
import com.serapercel.seraphine.util.toastShort

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var passwordAgain: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth= FirebaseAuth.getInstance()

        binding.btnSignUpSU.setOnClickListener {
            email = binding.etMailSU.text.toString()
            password = binding.etPasswordSU.text.toString()
            passwordAgain = binding.etPassword2.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && passwordAgain.isNotEmpty()) {
                if (password.length >= 6) {
                    if (password == passwordAgain) {
                        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
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

}