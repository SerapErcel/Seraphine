package com.serapercel.seraphine.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.serapercel.seraphine.R
import com.serapercel.seraphine.databinding.FragmentSignInBinding
import com.serapercel.seraphine.util.toastShort

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    lateinit var email: String
    private lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val view = binding.root
        auth = FirebaseAuth.getInstance()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignIn.setOnClickListener {
            email = binding.etMail.text.toString()
            password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                    findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
                }.addOnFailureListener {
                    requireContext().toastShort(it.message.toString())
                }
            } else {
                requireContext().toastShort("Please fill the fields!")
            }
        }

        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        binding.tvForgotPassword.setOnClickListener {
            email = binding.etMail.text.toString()
            if (email.isNotEmpty()) {
                auth.sendPasswordResetEmail(email).addOnSuccessListener {
                    requireContext().toastShort("Reset link has been sent to your e-mail address!")
                }.addOnFailureListener {
                    requireContext().toastShort(it.message.toString())
                }
            } else {
                requireContext().toastShort("Please fill the mail!")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            findNavController().navigate(R.id.action_signInFragment_to_homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
