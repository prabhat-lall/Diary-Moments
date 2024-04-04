package com.example.mynotes.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.mynotes.databinding.FragmentForgotPasswordBinding
import com.example.mynotes.utils.PasswordManager

class ForgotPasswordFragment : Fragment() {

    lateinit var binding: FragmentForgotPasswordBinding
    lateinit var passwordManager: PasswordManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        passwordManager = PasswordManager(requireContext())
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvFavQuest.text = passwordManager.getQuestion()

        val ans = binding.etAnswer.text
        val savedAnswer = passwordManager.getAnswer()
        binding.btnConfirm.setOnClickListener {
            //Toast.makeText(requireContext(),"$ans",Toast.LENGTH_SHORT ).show()
            if(ans.toString() == savedAnswer){
                Toast.makeText(requireContext(),"Password:${passwordManager.getPassword()}",Toast.LENGTH_LONG ).show()
            }else{
                Toast.makeText(requireContext(),"Wrong Answer",Toast.LENGTH_SHORT ).show()
                //Toast.makeText(requireContext(),"${passwordManager.getPassword()}",Toast.LENGTH_SHORT).show()
            }

        }

        binding.ivClose.setOnClickListener {
            //Toast.makeText(requireContext(),"back button placed",Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }






}