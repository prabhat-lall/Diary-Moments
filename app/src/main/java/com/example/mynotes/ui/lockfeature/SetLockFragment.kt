package com.example.mynotes.ui.lockfeature

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.mynotes.R
import com.example.mynotes.databinding.FragmentSetLockBinding
import com.example.mynotes.utils.PasswordManager


class SetLockFragment : Fragment() {

    lateinit var binding: FragmentSetLockBinding
    lateinit var passwordManager: PasswordManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetLockBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        passwordManager = PasswordManager(requireContext())

        clickListeners()

    }

    private fun clickListeners() {
        onBackPress()
        savePassword()
    }

    private fun savePassword() {
        val pass = binding.etPass.text
        val ans = binding.etQuestion.text

        binding.btnSave.setOnClickListener {
           // Toast.makeText(requireContext(), "$pass", Toast.LENGTH_SHORT)
            if (!pass.isNullOrEmpty() && !ans.isNullOrEmpty()) {
                if(pass.toString() != ans.toString()){
                    Toast.makeText(requireContext(), "Password not matching", Toast.LENGTH_SHORT)
                        .show()
                    binding.etPass.requestFocus()
                }else {
                    passwordManager.savePassword(pass.toString(), ans.toString())
                    Toast.makeText(
                        requireContext(),
                        "PassWord Saved Successfully",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_setLockFragment_to_securityQuestionFragment)
                }
            } else if (pass.isNullOrEmpty() && !ans.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please Fill PassWord", Toast.LENGTH_SHORT).show()
            } else if (!pass.isNullOrEmpty() && ans.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Please Fill Answer", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(requireContext(), "Please Fill both", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onBackPress() {
        binding.ivClose.setOnClickListener {
            Navigation.findNavController(requireView()).popBackStack()
        }
    }

}