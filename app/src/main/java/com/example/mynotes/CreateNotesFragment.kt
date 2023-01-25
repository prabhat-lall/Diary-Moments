package com.example.mynotes

import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.mynotes.databinding.FragmentCreateNotesBinding
import com.example.mynotes.model.Notes
import com.example.mynotes.viewmodel.NotesViewModel
import java.text.SimpleDateFormat
import java.util.*

class CreateNotesFragment : Fragment() {
    lateinit var binding:FragmentCreateNotesBinding
    val viewModel: NotesViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentCreateNotesBinding.inflate(layoutInflater, container, false)

        binding.btnSaveNotes.setOnClickListener {
            createNotes(it)
        }


        return binding.root
    }

    private fun createNotes(it: View?){

        val title = binding.edtTitle.text.toString()
        val subTitle = binding.edtSubtitle.text.toString()
        val notes = binding.edtSubtitle.text.toString()

        val date = Date()
        val notesDate : CharSequence = DateFormat.format("MMMM d , YYYY",date.time)

        val data = Notes(
            id = null,title = title,subTitle = subTitle,notes = notes,date = notesDate.toString()
        )

        viewModel.addNotes(data)

        Toast.makeText(requireContext(),"added Successfully",Toast.LENGTH_LONG).show()

        Navigation.findNavController(it!!).navigate(R.id.action_createNotesFragment_to_homeFragment)
    }

}

//val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
//val currentDate = sdf.format(Date())