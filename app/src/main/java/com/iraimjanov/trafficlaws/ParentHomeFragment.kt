package com.iraimjanov.trafficlaws

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.iraimjanov.trafficlaws.databinding.FragmentParentHomeBinding

class ParentHomeFragment : Fragment() {
    lateinit var binding: FragmentParentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentParentHomeBinding.inflate(layoutInflater)

        showActivity()

        return binding.root
    }

    private fun showActivity() {
        binding.bottomNavigationView.itemIconTintList = null
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navigationController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navigationController)
    }

}