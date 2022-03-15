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
import com.iraimjanov.trafficlaws.databinding.ContactItemBinding
import com.iraimjanov.trafficlaws.databinding.DeleteDialogBinding
import com.iraimjanov.trafficlaws.databinding.ViewPagerItemRecyclerViewBinding
import com.iraimjanov.trafficlaws.models.RoadSign
import java.io.File

class RecyclerViewAdapter(
    val context: Context,
    private val arrayListRoadSign: ArrayList<RoadSign>,
    val rvClick: RVClick,
) :
    RecyclerView.Adapter<RecyclerViewAdapter.VH>() {

    lateinit var dialog: AlertDialog
    private var booleanAntiBag = true

    inner class VH(private var itemRV: ContactItemBinding) : RecyclerView.ViewHolder(itemRV.root) {
        fun onBind(roadSign: RoadSign, position: Int) {

            itemRV.tvName.text = roadSign.name

            if (roadSign.image != "") {
                val file = File(context.filesDir, "${roadSign.image}.jpg")
                Glide.with(context).load(file).centerCrop().into(itemRV.imageProfile)
            }else{
                itemRV.imageProfile.setImageResource(0)
            }

            if (roadSign.like.toBoolean()) {
                itemRV.imageLike.setImageResource(R.drawable.ic_liked)
            } else {
                itemRV.imageLike.setImageResource(R.drawable.ic_like)
            }

            itemRV.imageLike.setOnClickListener {
                rvClick.like(roadSign, position)
            }

            itemRV.lyEdit.setOnClickListener {
                rvClick.edit(roadSign)
            }

            itemRV.lyDelete.setOnClickListener {
                if (booleanAntiBag) {
                    buildDeleteDialog(roadSign , position)
                    booleanAntiBag = false
                }
            }

            itemRV.root.setOnClickListener {
                rvClick.show(roadSign)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(arrayListRoadSign[position], position)

    }

    override fun getItemCount(): Int = arrayListRoadSign.size

    fun buildDeleteDialog(
        roadSign: RoadSign,
        position: Int,
    ) {

        val bindingDialog = DeleteDialogBinding.inflate(LayoutInflater.from(context))
        val alertDialog = AlertDialog.Builder(context)

        val file = File(context.filesDir, "${roadSign.image}.jpg")
        Glide.with(context).load(file).centerCrop().into(bindingDialog.imageProfile)

        bindingDialog.tvCancel.setOnClickListener {
            dialog.cancel()
        }

        bindingDialog.tvDelete.setOnClickListener {
            deleteRoadSign(roadSign, position)
        }

        alertDialog.setOnCancelListener {
            booleanAntiBag = true
        }

        alertDialog.setView(bindingDialog.root)
        dialog = alertDialog.create()

        dialog.window!!.attributes.windowAnimations = R.style.MyAnimation
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun deleteRoadSign(
        roadSign: RoadSign,
        position: Int,
    ) {
        val file = File(context.filesDir, "${roadSign.image}.jpg")
        if (context.filesDir.isDirectory) {
            for (i in context.filesDir.listFiles().indices) {
                if (context.filesDir.listFiles()[i] == file) {
                    context.filesDir.listFiles()[i].delete()
                    break
                }
            }
        }
        Toast.makeText(context, "O'chirildi", Toast.LENGTH_SHORT).show()
        dialog.cancel()

        rvClick.delete(roadSign, position)
        arrayListRoadSign.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeRemoved(position , arrayListRoadSign.size)
    }

    interface RVClick {
        fun like(roadSign: RoadSign, position: Int) {

        }

        fun delete(roadSign: RoadSign , position: Int) {

        }

        fun edit(roadSign: RoadSign) {

        }

        fun show(roadSign: RoadSign) {

        }
    }

}