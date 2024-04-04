package com.example.mynotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.mynotes.databinding.FragmentSearchBinding
import com.example.mynotes.databinding.FragmentSettingBinding
import com.example.mynotes.utils.PasswordManager

class SettingFragment : Fragment() {

    lateinit var binding: FragmentSettingBinding
    lateinit var passwordManager : PasswordManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        passwordManager = PasswordManager(requireContext())
        binding.ivClose.setOnClickListener {
            Navigation.findNavController(requireView()).popBackStack()
        }

        binding.switchOnForTheme.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                //Toast.makeText(requireContext(),"ON", Toast.LENGTH_SHORT).show()
                passwordManager.setAutoThemeOn(true)
            }else{
                //Toast.makeText(requireContext(),"OFF", Toast.LENGTH_SHORT).show()
                passwordManager.setAutoThemeOn(false)
            }
        }

        binding.switchOnForTheme.isChecked = passwordManager.isAutoThemeOn()

        binding.clSetName.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.action_settingFragment_to_profileSettingFragment)
        }
    }

}