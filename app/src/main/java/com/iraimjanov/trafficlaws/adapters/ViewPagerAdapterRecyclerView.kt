package com.iraimjanov.trafficlaws.adapters

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iraimjanov.trafficlaws.R
import com.iraimjanov.trafficlaws.databinding.DeleteDialogBinding
import com.iraimjanov.trafficlaws.databinding.ViewPagerItemRecyclerViewBinding
import com.iraimjanov.trafficlaws.models.RoadSign
import java.io.File

class ViewPagerAdapterRecyclerView(
    val context: Context,
    private val arrayListTypes: ArrayList<String>,
    private val hashMap: HashMap<String, ArrayList<RoadSign>>,
    private val rvClick: RecyclerViewAdapter.RVClick,
) :
    RecyclerView.Adapter<ViewPagerAdapterRecyclerView.VH>() {

    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    inner class VH(private val itemRV: ViewPagerItemRecyclerViewBinding) :
        RecyclerView.ViewHolder(itemRV.root) {
        fun onBind(positionTypes: Int) {

            recyclerViewAdapter = RecyclerViewAdapter(context,
                hashMap[arrayListTypes[positionTypes]]!!,
                object : RecyclerViewAdapter.RVClick {

                    override fun like(roadSign: RoadSign, position: Int) {
                        rvClick.like(roadSign, position)
                        itemRV.root.adapter!!.notifyItemChanged(position)
                    }

                    override fun delete(roadSign: RoadSign, position: Int) {
                        rvClick.delete(roadSign , position)
                    }

                    override fun edit(roadSign: RoadSign) {
                        rvClick.edit(roadSign)
                    }

                    override fun show(roadSign: RoadSign) {
                        rvClick.show(roadSign)
                    }

                })
            itemRV.root.adapter = recyclerViewAdapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ViewPagerItemRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int = arrayListTypes.size
}