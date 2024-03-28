package com.example.mynotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.mynotes.databinding.FragmentThemeBinding


class ThemeFragment : Fragment() {

    lateinit var binding : FragmentThemeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentThemeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.btnTheme.setOnClickListener {
//            Navigation.findNavController(requireView()).navigate(R.id.action_themeFragment_to_chooseThemeFragment)
//        }
    }

}