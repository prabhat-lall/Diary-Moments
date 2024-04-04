package com.example.mynotes.ui.lockfeature

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.mynotes.R
import com.example.mynotes.databinding.FragmentLockBinding
import com.example.mynotes.utils.PasswordManager
import java.util.concurrent.Executor

private const val TAG = "LockFragment"
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
        Log.d(TAG, "onViewCreated: ")
        passwordManager = PasswordManager(requireContext())
        initUI()
        handleClickListeners()

    }

    private fun initUI() {
        if(passwordManager.isLockOn()){
            binding.clSetSecurityQ.visibility = View.VISIBLE
            binding.clSetPassword.visibility = View.VISIBLE
        }else{
            binding.clSetSecurityQ.visibility = View.GONE
            binding.clSetPassword.visibility = View.GONE
        }


        //Toast.makeText(requireContext(),"abc ${binding.switchOn.isChecked}",Toast.LENGTH_SHORT).show()


    }

    private fun handleClickListeners() {

        binding.switchOn.setOnClickListener {
            val isChecked = binding.switchOn.isChecked
            if (isChecked) {
                if(passwordManager.getPassword().isNullOrEmpty()) {
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_lockFragment_to_setLockFragment)
                }else{
                    passwordManager.saveLockOn(true)
                }
                binding.clSetSecurityQ.isVisible = true
                binding.clSetPassword.isVisible = true
            } else {
                passwordManager.apply {
                    saveLockOn(false)
                }
                binding.clSetSecurityQ.isGone = true
                binding.clSetPassword.isGone = true
               // binding.fingerPrintSwitchOn.isChecked = false
            }
        }

        //binding.fingerPrintSwitchOn.isChecked = passwordManager.isBiometricLockOn()

//        binding.fingerPrintSwitchOn.setOnCheckedChangeListener { buttonView, isBiometricChecked ->
//            if(isBiometricChecked){
//                if(!binding.switchOn.isChecked){
//                    Toast.makeText(requireContext(),"Please Set Diary lock first.",Toast.LENGTH_SHORT).show()
//                }else {
//                    Toast.makeText(requireContext(), "ON", Toast.LENGTH_SHORT).show()
//                    passwordManager.saveBiometricLockOn(true)
//                    binding.switchOn.isChecked = true
//                }
//            }else{
//                Toast.makeText(requireContext(),"OFF",Toast.LENGTH_SHORT).show()
//                passwordManager.saveBiometricLockOn(false)
//            }
//        }

        binding.clSetPassword.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_lockFragment_to_setLockFragmentSecond)
        }

        binding.ivClose.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.clSetSecurityQ.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_lockFragment_to_securityQuestionFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.switchOn.isChecked = passwordManager.isLockOn()

    }

}