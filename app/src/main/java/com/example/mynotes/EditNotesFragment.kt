package com.example.mynotes

import android.os.Bundle
import android.text.format.DateFormat
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mynotes.databinding.FragmentEditNotesBinding
import com.example.mynotes.model.Notes
import com.example.mynotes.viewmodel.NotesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*


class EditNotesFragment : Fragment() {

    val oldNotes by navArgs<EditNotesFragmentArgs>()
    private lateinit var binding: FragmentEditNotesBinding
    private val viewModel: NotesViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditNotesBinding.inflate(layoutInflater,container,false)
        setHasOptionsMenu(true)
        binding.edtTitle.setText(oldNotes.data.title)
        binding.edtSubtitle.setText(oldNotes.data.subTitle)
        binding.edtNotes.setText(oldNotes.data.notes)

        binding.btnDone.setOnClickListener {
            updateNotes(it)
        }

        return binding.root
    }

    private fun updateNotes(it:View?){
        val title = binding.edtTitle.text.toString()
        val subTitle = binding.edtSubtitle.text.toString()
        val notes = binding.edtSubtitle.text.toString()

        val date = Date()
        val notesDate : CharSequence = DateFormat.format("MMMM d  ,YYYY",date.time)

        val data = Notes(
            id = oldNotes.data.id,title = title,subTitle = subTitle,notes = notes,date = notesDate.toString()
        )

        viewModel.updateNotes(data)

        Toast.makeText(requireContext(),"notes updated Successfully", Toast.LENGTH_SHORT).show()

        Navigation.findNavController(it!!).navigate(R.id.action_editNotesFragment_to_homeFragment)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete){
            val bottomSheet: BottomSheetDialog = BottomSheetDialog(requireContext())
            bottomSheet.setContentView(R.layout.dialog_delete)
            val buttonYes = bottomSheet.findViewById<Button>(R.id.yes_dialog)
            val buttonNo = bottomSheet.findViewById<Button>(R.id.no_dialog)

            buttonYes?.setOnClickListener {
                viewModel.deleteNotes(oldNotes.data.id!!)
                //Navigation.findNavController(it).navigate(R.id.action_editNotesFragment_to_homeFragment)
                findNavController().popBackStack()
                bottomSheet.dismiss()
            }

            buttonNo?.setOnClickListener {
                bottomSheet.dismiss()
            }

            bottomSheet.show()

        }
        return super.onOptionsItemSelected(item)
    }
}