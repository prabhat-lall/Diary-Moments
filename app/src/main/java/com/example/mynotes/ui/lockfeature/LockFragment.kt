package com.example.mynotes.ui.lockfeature

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.mynotes.R
import com.example.mynotes.databinding.FragmentLockBinding
import com.example.mynotes.utils.PasswordManager
import java.util.concurrent.Executor


class LockFragment : Fragment() {

    lateinit var binding: FragmentLockBinding
    lateinit var passwordManager: PasswordManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        passwordManager = PasswordManager(requireContext())
        initUI()
        handleClickListeners()

    }

    private fun initUI() {
        if(passwordManager.isLockOn()){
            binding.clSetSecurityQ.visibility = View.VISIBLE
            binding.clSetPassword.visibility = View.VISIBLE
        }
        if(passwordManager.getPassword().isNullOrEmpty()){
            binding.switchOn.isChecked = false
        }else{
            binding.switchOn.isChecked = true
        }

    }

    private fun handleClickListeners() {
        binding.clSetPassword.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_lockFragment_to_setLockFragment)
        }

        binding.ivClose.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.clSetSecurityQ.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_lockFragment_to_securityQuestionFragment)
        }


        binding.switchOn.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                Toast.makeText(requireContext(),"switch ON",Toast.LENGTH_SHORT).show()
                passwordManager.saveLockOn(true)
                binding.clSetSecurityQ.visibility = View.VISIBLE
                binding.clSetPassword.visibility = View.VISIBLE
                if(passwordManager.getPassword().equals("")){
                    Navigation.findNavController(requireView()).navigate(R.id.action_lockFragment_to_setLockFragment)
                }
            }else{
                Toast.makeText(requireContext(),"switch OFF",Toast.LENGTH_SHORT).show()
                passwordManager.saveLockOn(false)
                binding.clSetSecurityQ.visibility = View.GONE
                binding.clSetPassword.visibility = View.GONE
                binding.fingerPrintSwitchOn.isChecked = false
            }

        }
        binding.fingerPrintSwitchOn.isChecked = passwordManager.isBiometricLockOn()

        binding.fingerPrintSwitchOn.setOnCheckedChangeListener { buttonView, isBiometricChecked ->
            if(isBiometricChecked){
                if(!binding.switchOn.isChecked){
                    Toast.makeText(requireContext(),"Please Set Diary lock first.",Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(requireContext(), "ON", Toast.LENGTH_SHORT).show()
                    passwordManager.saveBiometricLockOn(true)
                    binding.switchOn.isChecked = true
                }
            }else{
                Toast.makeText(requireContext(),"OFF",Toast.LENGTH_SHORT).show()
                passwordManager.saveBiometricLockOn(false)
            }
        }

    }


}