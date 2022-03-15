package com.iraimjanov.trafficlaws

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.iraimjanov.trafficlaws.adapters.RecyclerViewLikeAdapter
import com.iraimjanov.trafficlaws.databinding.FragmentFavoriteBinding
import com.iraimjanov.trafficlaws.db.MyDBHelper
import com.iraimjanov.trafficlaws.models.RoadSign

class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var arrayListRoadSign: ArrayList<RoadSign>
    private lateinit var recyclerViewLikeAdapter: RecyclerViewLikeAdapter
    private lateinit var myDBHelper: MyDBHelper
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        loadData()
        return binding.root
    }

    private fun loadData() {
        Object.roadSign = null
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerViewParent) as NavHostFragment
        navController = navHostFragment.navController
        myDBHelper = MyDBHelper(requireActivity())
        arrayListRoadSign = ArrayList()
        arrayListRoadSign = myDBHelper.showRoadSign()
        arrayListRoadSign = sortArrayList(arrayListRoadSign)
        recyclerViewLikeAdapter = RecyclerViewLikeAdapter(requireActivity(),
            arrayListRoadSign,
            object : RecyclerViewLikeAdapter.RVClickLike {
                override fun like(roadSign: RoadSign, position: Int) {
                    myDBHelper.updateRoadSign(roadSign)
                }

                override fun delete(roadSign: RoadSign, position: Int) {
                    myDBHelper.deleteRoadSign(roadSign)
                }

                override fun edit(roadSign: RoadSign) {
                    Object.roadSign = roadSign
                    navController.navigate(R.id.action_parentHomeFragment_to_addFragment)
                }

                override fun show(roadSign: RoadSign) {
                    Object.roadSign = roadSign
                    navController.navigate(R.id.action_parentHomeFragment_to_showFragment)
                }

            })
        binding.recyclerViewRoadSign.adapter = recyclerViewLikeAdapter
    }

    private fun sortArrayList(arrayListRoadSign: java.util.ArrayList<RoadSign>): java.util.ArrayList<RoadSign> {
        val arrayList = ArrayList<RoadSign>()
        for (i in arrayListRoadSign) {
            if (i.like.toBoolean()) {
                arrayList.add(i)
            }
        }
        return arrayList
    }

}