package com.example.mynotes

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mynotes.adapter.NotesAdapter
import com.example.mynotes.databinding.FragmentHomeBinding
import com.example.mynotes.databinding.FragmentSearchBinding
import com.example.mynotes.viewmodel.NotesViewModel


class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    private val viewModel: NotesViewModel by viewModels()

    lateinit var adapter : NotesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.searchView.requestFocus()
//        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.showSoftInput(binding.searchView, InputMethodManager.SHOW_IMPLICIT)

        setHasOptionsMenu(true)
        backClick()
            binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    notesFiltering(newText)
                    return true
                }

            })




    }

    private fun notesFiltering(newText:String?) {

    }

    private fun backClick() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()

        }
    }




}