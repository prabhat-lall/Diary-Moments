package com.example.mynotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mynotes.databinding.FragmentEditProfileBottomSheetBinding
import com.example.mynotes.model.EditData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditProfileBottomSheetFragment : BottomSheetDialogFragment() {

    lateinit var binding: FragmentEditProfileBottomSheetBinding

    private lateinit var  editProfileBottomSheetInterface : EditProfileBottomSheetFragmentInterface


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        var name = binding.etName.text.toString()
//        var quote = binding.etQuote.text.toString()
//        var author = binding.etName.text.toString()
//        val editData = EditData(name,quote,author)
        binding.btnOk.setOnClickListener {
            editProfileBottomSheetInterface.setData(binding.etName.text.toString())
            this.dialog?.dismiss()
        }


//        if(checkFormValidation()){
//
//        }
    }

//    private fun checkFormValidation(): Boolean {
//        var isCorrected = false
//        if(binding.etName.text.toString()){
//
//        }
//    }

    fun setInterface(editProfileBottomSheetInterfaceInterface : EditProfileBottomSheetFragmentInterface){
        editProfileBottomSheetInterface = editProfileBottomSheetInterfaceInterface
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditProfileBottomSheetFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    interface EditProfileBottomSheetFragmentInterface {
        fun setData(name: String)
    }
}