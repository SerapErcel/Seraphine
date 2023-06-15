package com.serapercel.seraphine.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.serapercel.seraphine.configs.ApiClient
import com.serapercel.seraphine.databinding.FragmentHomeBinding
import com.serapercel.seraphine.model.MusicCategory
import com.serapercel.seraphine.model.MusicResponse
import com.serapercel.seraphine.service.MusicService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var service: MusicService
    lateinit var list: List<MusicCategory>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        service = ApiClient.getClient().create(MusicService::class.java)

        service.allMusics().enqueue(object : Callback<MusicResponse> {
            override fun onResponse(call: Call<MusicResponse>, response: Response<MusicResponse>) {
                val datas = response.body()
                list = datas!!.musicCategories!!
                Log.d("get music", list[1].baseTitle.toString())
            }

            override fun onFailure(call: Call<MusicResponse>, t: Throwable) {
                Toast.makeText(requireContext(), t.message.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}