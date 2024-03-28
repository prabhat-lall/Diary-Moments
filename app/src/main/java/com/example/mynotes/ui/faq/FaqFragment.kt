package com.example.mynotes.ui.faq

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynotes.adapter.FaqAdapter
import com.example.mynotes.databinding.FragmentCreateNotesBinding
import com.example.mynotes.databinding.FragmentFaqBinding
import com.example.mynotes.model.Faq


class FaqFragment : Fragment() {

    lateinit var binding: FragmentFaqBinding
    lateinit var adapter: FaqAdapter
    val faqList = mutableListOf<Faq>(Faq("what is my diary name?", "My diary name is Lock Diary"),
        Faq("what is my name?", "My diary name is khan"),
        Faq("How to forget password?", "You can change your password for security reasons or reset it if you forget it. Your Google Account password is used to access many Google products, like Gmail and YouTube."),
        Faq("what is my diary name?", "My diary name is Lock Diary"),
        Faq("what is my diary name?", "My diary name is Lock Diary")
        )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFaqBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleClickListener()
        adapter = FaqAdapter(faqList)
        binding.rvFaq.adapter = adapter
        binding.rvFaq.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

    }

    private fun handleClickListener() {
        binding.ivClose.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}