package com.iraimjanov.trafficlaws

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.iraimjanov.trafficlaws.databinding.FragmentInfoBinding
import java.io.*


class InfoFragment : Fragment() {
    lateinit var binding: FragmentInfoBinding

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentInfoBinding.inflate(layoutInflater)
        binding.tvAppNumber.text = "Ilova raqami ${BuildConfig.VERSION_NAME}"
        binding.cardShare.setOnClickListener {
            sendApk()
        }
        return binding.root
    }

    private fun shareApp() {
        val api = requireActivity().applicationInfo
        val filePath = api.sourceDir

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "application/vnd.android.package-archive"

        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(filePath));
        startActivity(Intent.createChooser(intent, "Share Using"))
    }

    private fun sendApk() {
        val app = requireActivity().applicationContext.applicationInfo
        val filePath = app.sourceDir
        val intent = Intent(Intent.ACTION_SEND)

        // MIME of .apk is "application/vnd.android.package-archive".
        // but Bluetooth does not accept this. Let's use "*/*" instead.
        intent.type = "application/vnd.android.package-archive"

        // Append file and send Intent
        val originalApk = File(filePath)

        //Make new directory in new location
        var tempFile = File(requireActivity().externalCacheDir.toString() + "/ExtractedApk")
        //If directory doesn't exists create new
        if (!tempFile.isDirectory) {
            if (!tempFile.mkdirs()) {
                return
            }
        }
        //Get application's name and convert to lowercase
        tempFile = File(tempFile.path.toString() + "/" + requireActivity().getString(app.labelRes)
            .replace(" ", "").lowercase() + ".apk")
        //If file doesn't exists create new
        if (!tempFile.exists()) {
            if (!tempFile.createNewFile()) {
                return
            }
        }
        //Copy file to new location
        val `in`: InputStream = FileInputStream(originalApk)
        val out: OutputStream = FileOutputStream(tempFile)
        val buf = ByteArray(1024)
        var len: Int
        while (`in`.read(buf).also { len = it } > 0) {
            out.write(buf, 0, len)
        }
        `in`.close()
        out.close()
        println("File copied.")
        //Open share dialog
        val uri = FileProvider.getUriForFile(context!!, requireActivity().packageName, tempFile)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        requireActivity().grantUriPermission(requireActivity().packageManager.toString(),
            uri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        requireActivity().startActivity(intent)

    }

}