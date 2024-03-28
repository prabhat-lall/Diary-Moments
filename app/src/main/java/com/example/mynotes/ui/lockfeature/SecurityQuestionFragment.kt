package com.example.mynotes.ui.lockfeature

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.mynotes.R
import com.example.mynotes.databinding.FragmentSecurityQuestionBinding
import com.example.mynotes.utils.PasswordManager


class SecurityQuestionFragment : Fragment()   {

    lateinit var binding : FragmentSecurityQuestionBinding
    lateinit var passwordManager : PasswordManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        passwordManager = PasswordManager(requireContext())
        // Inflate the layout for this fragment
        binding = FragmentSecurityQuestionBinding.inflate(layoutInflater,container , false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val questions = arrayOf("What is your favourite movie","What is your favourite food","What is your favourite color")
        val adapter = ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,questions)
        binding.spinnerQuestion.adapter = adapter
        binding.spinnerQuestion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                passwordManager.saveQuestion(question = questions[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        binding.ivClose.setOnClickListener {
            findNavController().popBackStack()
        }

        saveAnswer()

    }

    private fun saveAnswer() {
        val ans = binding.etAnswer.text

        binding.btnConfirmAnswer.setOnClickListener {
            // Toast.makeText(requireContext(), "$pass", Toast.LENGTH_SHORT)
            if (!ans.isNullOrEmpty()) {
                passwordManager.saveAnswer(ans.toString())
                Toast.makeText(requireContext(), "Answer Saved Successfully", Toast.LENGTH_SHORT)
                    .show()
               // Navigation.findNavController(requireView()).navigate(R.id.action_securityQuestionFragment_to_lockFragment)
                findNavController().popBackStack()
                findNavController().popBackStack()
            }else{
                Toast.makeText(requireContext(), "Please Fill Answer", Toast.LENGTH_SHORT).show()
            }
        }
    }



}