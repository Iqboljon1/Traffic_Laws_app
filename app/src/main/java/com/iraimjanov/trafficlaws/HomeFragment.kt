package com.iraimjanov.trafficlaws

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.iraimjanov.trafficlaws.adapters.RecyclerViewAdapter
import com.iraimjanov.trafficlaws.adapters.ViewPagerAdapterRecyclerView
import com.iraimjanov.trafficlaws.databinding.*
import com.iraimjanov.trafficlaws.db.MyDBHelper
import com.iraimjanov.trafficlaws.models.RoadSign

class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    lateinit var navHostFragment: NavHostFragment
    lateinit var navController: NavController
    lateinit var arrayListTypes: ArrayList<String>
    lateinit var hashMap: HashMap<String, ArrayList<RoadSign>>
    lateinit var arrayListRoadSign: ArrayList<RoadSign>
    lateinit var myDBHelper: MyDBHelper
    lateinit var viewPagerAdapterRecyclerView: ViewPagerAdapterRecyclerView
    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        loadData()

        recyclerViewAdapter = RecyclerViewAdapter(requireActivity(),
            arrayListRoadSign,
            object : RecyclerViewAdapter.RVClick {})

        viewPagerAdapterRecyclerView =
            ViewPagerAdapterRecyclerView(requireActivity(), arrayListTypes, hashMap,
                object : RecyclerViewAdapter.RVClick {

                    override fun like(roadSign: RoadSign, position: Int) {
                        clickLike(roadSign)
                    }

                    override fun delete(roadSign: RoadSign, position: Int) {
                        myDBHelper.deleteRoadSign(roadSign)
                        loadData()
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

        binding.viewPager.adapter = viewPagerAdapterRecyclerView

        binding.lyAdd.setOnClickListener {
            navController.navigate(R.id.action_parentHomeFragment_to_addFragment)
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val bindingSelected = TabItemSelectedBinding.inflate(layoutInflater)
                bindingSelected.tvName.text = tab!!.text
                tab.view.removeAllViews()
                tab.customView = bindingSelected.root
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val bindingUnselected = TabItemUnselectedBinding.inflate(layoutInflater)
                bindingUnselected.tvName.text = tab!!.text
                tab.view.removeAllViews()
                tab.customView = bindingUnselected.root
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val bindingSelected = TabItemSelectedBinding.inflate(layoutInflater)
                bindingSelected.tvName.text = tab!!.text
                tab.view.removeAllViews()
                tab.customView = bindingSelected.root
            }
        })

        buildTabLayout()

        return binding.root
    }

    private fun buildTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = arrayListTypes[position]
            val bindingUnselected = TabItemUnselectedBinding.inflate(layoutInflater)
            bindingUnselected.tvName.text = tab.text
            tab.view.removeAllViews()
            tab.customView = bindingUnselected.root
        }.attach()
    }

    private fun loadData() {
        Object.roadSign = null
        navHostFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.fragmentContainerViewParent) as NavHostFragment
        navController = navHostFragment.navController
        arrayListTypes = ArrayList()
        arrayListRoadSign = ArrayList()
        hashMap = HashMap()
        myDBHelper = MyDBHelper(requireActivity())
        arrayListTypes.add("Ogohlanturuvchi")
        arrayListTypes.add("Imtiyozli")
        arrayListTypes.add("Ta'qiqlovchi")
        arrayListTypes.add("Buyuruvchi")
        arrayListTypes.add("Axborot-ishora")
        arrayListTypes.add("Servis")
        arrayListTypes.add("Qo'shimcha axborot")
        arrayListRoadSign = myDBHelper.showRoadSign()
        hashMap = buildHashMap()
    }

    private fun buildHashMap(): HashMap<String, ArrayList<RoadSign>> {
        val hashMap = HashMap<String, ArrayList<RoadSign>>()
        for (i in arrayListTypes) {
            hashMap[i] = buildArrayListForHashMap(i)
        }
        return hashMap
    }

    private fun buildArrayListForHashMap(type: String): ArrayList<RoadSign> {
        val arrayList = ArrayList<RoadSign>()
        for (i in arrayListRoadSign) {
            if (i.type == type) {
                arrayList.add(i)
            }
        }
        return arrayList
    }

    fun clickLike(roadSign: RoadSign) {
        val boolean = roadSign.like.toBoolean()
        if (boolean) {
            roadSign.like = "false"
        } else {
            roadSign.like = "true"
        }
        myDBHelper.updateRoadSign(roadSign)
        loadData()
    }
}