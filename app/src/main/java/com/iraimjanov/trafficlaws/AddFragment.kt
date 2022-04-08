package com.iraimjanov.trafficlaws

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.iraimjanov.trafficlaws.databinding.FragmentAddBinding
import com.iraimjanov.trafficlaws.db.MyDBHelper
import com.iraimjanov.trafficlaws.models.RoadSign
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime


class AddFragment : Fragment() {
    private lateinit var arrayListTypes: ArrayList<String>
    private lateinit var arrayAdapterTypes: ArrayAdapter<String>
    private lateinit var binding: FragmentAddBinding
    private lateinit var myDBHelper: MyDBHelper
    private var time: String? = null
    private var uri: Uri? = null
    private var booleanEditImage = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = FragmentAddBinding.inflate(layoutInflater)
        loadData()
        binding.spinnerTypes.setAdapter(arrayAdapterTypes)

        binding.imageBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.cardAddPhoto.setOnClickListener {
            getImageContent.launch("image/*")
        }

        binding.imageSave.setOnClickListener {
            if (binding.edtName.text.toString().trim().isNotEmpty() && binding.spinnerTypes.text.toString().trim().isNotEmpty()) {
                val boolean = checkName(binding.edtName.text.toString().trim())
                if (boolean) {
                    if (Object.roadSign == null) {
                        saveRoadSign()
                    } else {
                        editRoadSign()
                    }
                } else {
                    if (Object.roadSign != null){
                        if (binding.edtName.text.toString().trim() != Object.roadSign!!.name) {
                            Toast.makeText(requireActivity(), "Ma'lumot saqlanmadi!\nChunki bu nomdagi belgi avvaldan mavjud", Toast.LENGTH_SHORT).show()
                        }else{
                            editRoadSign()
                        }
                    }else{
                        Toast.makeText(requireActivity(), "Ma'lumot saqlanmadi!\nChunki bu nomdagi belgi avvaldan mavjud", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireActivity(), "Ma'lumot yetarli emas", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun editRoadSign() {
        val id = Object.roadSign!!.id
        val name = binding.edtName.text.toString().trim()
        val type = binding.spinnerTypes.text.toString().trim()
        val about = binding.edtAbout.text.toString().trim()
        val like = Object.roadSign!!.like
        var image = Object.roadSign!!.image


        if (booleanEditImage) {
            image = if (uri != null) {
                deleteImageFromFilesDir()
                val inputStream = requireActivity().contentResolver?.openInputStream(uri!!)
                val file = File(requireActivity().filesDir, "$time.jpg")
                val fileOutputStream = FileOutputStream(file)
                inputStream?.copyTo(fileOutputStream)
                inputStream?.close()
                fileOutputStream.close()
                time.toString()
            } else {
                deleteImageFromFilesDir()
                ""
            }
        }
        
        val roadSign = RoadSign(id, name, about, type, image, like)
        val boolean = myDBHelper.updateRoadSign(roadSign)
        if (boolean) {
            Toast.makeText(requireActivity(), "Ma'lumot o'zgartirildi", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    private fun deleteImageFromFilesDir() {
        if (requireActivity().filesDir.isDirectory) {
            val file = File(requireActivity().filesDir, "${Object.roadSign!!.image}.jpg")
            for (i in requireActivity().filesDir.listFiles().indices) {
                if (requireActivity().filesDir.listFiles()[i] == file) {
                    requireActivity().filesDir.listFiles()[i].delete()
                    break
                }
            }
        }
    }

    private fun checkName(name: String): Boolean {
        val arrayList = myDBHelper.showRoadSign()
        val hashSet = HashSet<String>()
        for (i in arrayList) {
            hashSet.add(i.name!!)
        }
        return hashSet.add(name)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveRoadSign() {
        val name = binding.edtName.text.toString().trim()
        val type = binding.spinnerTypes.text.toString().trim()
        val about = binding.edtAbout.text.toString().trim()
        var image = ""

        if (uri != null) {
            val inputStream = requireActivity().contentResolver?.openInputStream(uri!!)
            val file = File(requireActivity().filesDir, "${time.toString()}.jpg")
            val fileOutputStream = FileOutputStream(file)
            inputStream?.copyTo(fileOutputStream)
            inputStream?.close()
            fileOutputStream.close()
            image = time.toString()
        }
        val roadSign = RoadSign(name, about, type, image, "false")
        myDBHelper.addRoadSign(roadSign)

        binding.edtName.text.clear()
        binding.edtAbout.text.clear()
        binding.spinnerTypes.text.clear()
        binding.imagePhoto.setImageResource(0)
        time = buildPath(LocalDateTime.now().toString())
        val layoutParams: ViewGroup.LayoutParams = binding.imagePhoto.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        binding.imagePhoto.layoutParams = layoutParams
        Toast.makeText(requireActivity(), "Saqlandi", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadData() {
        time = buildPath(LocalDateTime.now().toString())
        myDBHelper = MyDBHelper(requireActivity())
        arrayListTypes = ArrayList()
        arrayListTypes.add("Ogohlanturuvchi")
        arrayListTypes.add("Imtiyozli")
        arrayListTypes.add("Ta'qiqlovchi")
        arrayListTypes.add("Buyuruvchi")
        arrayListTypes.add("Axborot-ishora")
        arrayListTypes.add("Servis")
        arrayListTypes.add("Qo'shimcha axborot")
        arrayAdapterTypes = ArrayAdapter(requireActivity(), R.layout.item_spinner, arrayListTypes)

        // edit mode
        if (Object.roadSign != null) {
            binding.actionBarTitle.text = "Yo’l belgisini o'zgartirish"
            binding.imageSave.setImageResource(R.drawable.ic_edit_white)
            binding.spinnerTypes.setText(Object.roadSign!!.type)
            binding.edtName.setText(Object.roadSign!!.name)
            binding.edtAbout.setText(Object.roadSign!!.about)
            binding.imageDeleteImage.visibility = View.VISIBLE

            // install image in imageView
            if (Object.roadSign!!.image!!.isNotEmpty()) {
                val file = File(requireActivity().filesDir, "${Object.roadSign!!.image}.jpg")
                Glide.with(requireActivity()).load(file).centerCrop().into(binding.imagePhoto)
                val layoutParams: ViewGroup.LayoutParams = binding.imagePhoto.layoutParams
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                binding.imagePhoto.layoutParams = layoutParams
                uri = Uri.fromFile(file)
            }

            binding.imageDeleteImage.setOnClickListener {
                binding.imagePhoto.setImageResource(0)
                val layoutParams: ViewGroup.LayoutParams = binding.imagePhoto.layoutParams
                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                binding.imagePhoto.layoutParams = layoutParams
                uri = null
                booleanEditImage = true
            }

        }

    }

    private fun buildPath(time: String): String {
        var path = ""
        for (i in time) {
            if (i.toString() != "-" && i.toString() != "T" && i.toString() != ":" && i.toString() != ".") {
                path += i
            }
        }
        return path
    }

    private val getImageContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it ?: return@registerForActivityResult
        Glide.with(requireActivity()).load(it).centerCrop().into(binding.imagePhoto)
        val layoutParams: ViewGroup.LayoutParams = binding.imagePhoto.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        binding.imagePhoto.layoutParams = layoutParams
        uri = it
        booleanEditImage = true
    }
}