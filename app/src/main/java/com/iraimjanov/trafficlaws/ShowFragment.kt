package com.iraimjanov.trafficlaws

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.iraimjanov.trafficlaws.adapters.RecyclerViewAdapter
import com.iraimjanov.trafficlaws.databinding.FragmentShowBinding
import com.iraimjanov.trafficlaws.db.MyDBHelper
import com.iraimjanov.trafficlaws.models.RoadSign
import java.io.File

class ShowFragment : Fragment() {
    private lateinit var binding: FragmentShowBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentShowBinding.inflate(layoutInflater)

        if (Object.roadSign!!.image != ""){
            val file = File(requireActivity().filesDir , "${Object.roadSign!!.image}.jpg")
            Glide.with(requireActivity()).load(file).centerCrop().into(binding.imagePhoto)
        }else{
            binding.imagePhoto.setImageResource(0)
        }

        binding.tvAbout.text = Object.roadSign!!.about
        binding.actionBarTitle.text = Object.roadSign!!.name

        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }
}