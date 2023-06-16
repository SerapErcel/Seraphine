package com.serapercel.seraphine.ui.fragments

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.activity.OnBackPressedCallback
import com.serapercel.seraphine.databinding.FragmentPlayBinding
import com.serapercel.seraphine.model.Music
import com.serapercel.seraphine.util.toastLong
import androidx.navigation.fragment.findNavController
import com.serapercel.seraphine.R

class PlayFragment : Fragment() {
    private var _binding: FragmentPlayBinding? = null
    private val binding get() = _binding!!
    private lateinit var music: Music

    lateinit var mediaPlayer: MediaPlayer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayBinding.inflate(inflater, container, false)
        music = arguments?.getSerializable("music") as Music

        try {
            val uri = Uri.parse(music.url)
            mediaPlayer = MediaPlayer.create(requireContext(), uri)
        } catch (e: Exception) {
            requireContext().toastLong(e.message.toString())
        }
        val volume = getVolume()
        mediaPlayer.setVolume(volume, volume)
        binding.sbSound.progress = (volume * 100).toInt()
        mediaPlayer.start()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvMusicTitle.text = music.title
        binding.btnPause.setOnClickListener {
            mediaPlayer.pause()
            binding.btnPause.visibility = View.INVISIBLE
            binding.btnPlay.visibility = View.VISIBLE
            binding.lottieAnimation.visibility = View.GONE
        }

        binding.btnPlay.setOnClickListener {
            mediaPlayer.start()
            binding.btnPause.visibility = View.VISIBLE
            binding.btnPlay.visibility = View.INVISIBLE
            binding.lottieAnimation.visibility = View.VISIBLE
        }

        binding.sbSound.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val volume = progress.toFloat() / 100
                mediaPlayer.setVolume(volume, volume)
                saveVolume(volume)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_playFragment_to_homeFragment)
                    mediaPlayer.stop()
                }
            })
    }


    private fun saveVolume(volume: Float) {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putFloat("Volume", volume)
        editor.apply()
    }

    private fun getVolume(): Float {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getFloat("Volume", 0.5f)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}