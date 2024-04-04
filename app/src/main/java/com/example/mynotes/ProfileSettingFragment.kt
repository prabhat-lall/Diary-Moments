package com.example.mynotes

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.example.mynotes.databinding.FragmentProfileSettingBinding
import com.example.mynotes.model.EditData
import com.example.mynotes.utils.PasswordManager
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileSettingFragment : Fragment(),
    SelectImageBottomSheetFragment.SelectImageBottomSheetDialogInterface, EditProfileBottomSheetFragment.EditProfileBottomSheetFragmentInterface {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var binding: FragmentProfileSettingBinding
    private lateinit var imageUri : Uri

    lateinit var passwordManager: PasswordManager


    private val imageContractForCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) {
            try {
                val inputStream = requireActivity().contentResolver.openInputStream(imageUri)
                val buffer = ByteArray(inputStream!!.available())
                inputStream.read(buffer)
                inputStream.close()

                val filename = "profile_photo.png" // You can choose any filename and extension
                val fileOutputStream =
                    requireActivity().openFileOutput(filename, Context.MODE_PRIVATE)
                fileOutputStream.write(buffer)
                fileOutputStream.close()
                binding.ivProfile.setImageURI(null)
                binding.ivProfile.setImageURI(imageUri)


            } catch (e: Exception) {
                Log.e(TAG, "${e.toString()}")
            }
        }

    private val imageContract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        Log.d("_prabhat", "HomeFragment:$it ")
        copyPhotoToPrivateStorage(it!!)
        if (it != null) {
            binding.ivProfile.setImageURI(it)
        }
    }

    private fun copyPhotoToPrivateStorage(sourceUri: Uri) {
        try {
            val contentResolver: ContentResolver = requireActivity().contentResolver

            // Create target URI using your file provider authority
            val targetUri =
                Uri.parse("content://com.example.mynotes.fileprovider/camera_photos/camera_photo.png")

            // Open output stream for writing to your app's private storage
            val outputStream: OutputStream? = contentResolver.openOutputStream(targetUri)
            if (outputStream != null) {
                // Open input stream for reading from the source
                val inputStream: InputStream? = contentResolver.openInputStream(sourceUri)

                // Copy data in chunks
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (inputStream?.read(buffer).also { bytesRead = it!! } != -1) {
                    outputStream.write(buffer, 0, bytesRead)
                }

                // Close streams (important to release resources)
                outputStream.close()
                inputStream?.close()

                // Show success message (optional)
                Toast.makeText(requireContext(), "Photo copied successfully!", Toast.LENGTH_SHORT).show()
            } else {
                // Handle error opening output stream
                Toast.makeText(requireContext(), "Failed to create output stream!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            // Handle potential I/O exceptions during copying
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error copying photo: " + e.message, Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        passwordManager = PasswordManager(requireContext())
//        if(!binding.etName.text.toString().isEmpty()){
//            passwordManager.setName(binding.etName.text.toString())
//        }
        if(passwordManager.getName().toString() != ""){
            binding.tvName.text = passwordManager.getName().toString()
        }
        binding.tvName.setOnClickListener {
            openEditBottomSheetFragment()
        }

        binding.tvNumberDiary.text = getString(R.string.no_of_diary,passwordManager.getNumberOfDiary().toString())

        binding.ivClose.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnEdit.setOnClickListener {
            openEditBottomSheetFragment()
        }

        imageUri = createImageUri()!!
        Log.d("_prabhat", "HomeFragmenthgygyg:$imageUri ")
        binding.ivProfile.setImageURI(imageUri)
        binding.ivProfileClick.setOnClickListener {
           // imageContractForCamera.launch(imageUri)
            val bottomSheetFragment = SelectImageBottomSheetFragment()
            bottomSheetFragment.setInterface(this)
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            bottomSheetFragment.show(fragmentManager, "BottomSheetFragment")
        }


    }

    private fun openEditBottomSheetFragment() {
        val bottomSheetFragment = EditProfileBottomSheetFragment()
        bottomSheetFragment.setInterface(this)
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        bottomSheetFragment.show(fragmentManager, "EditProfileBottomSheetFragment")
    }

    private fun createImageUri(): Uri? {
        val image = File(requireActivity().applicationContext.filesDir, "camera_photo.png")
        return FileProvider.getUriForFile(
            requireActivity().applicationContext,
            "com.example.mynotes.fileprovider",
            image
        )
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileSettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun selectCamera() {
        imageContractForCamera.launch(imageUri)
    }

    override fun selectGallery() {
        imageContract.launch("image/*")
    }

    override fun setData(name: String) {
//        binding.tvName.text = editData.name
//        binding.tvQuote.text = editData.quote
//        binding.tvAuthor.text = editData.author
        binding.tvName.text = name
        passwordManager.setName(name)
    }
}