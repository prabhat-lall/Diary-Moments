package com.example.mynotes

import android.app.AlertDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mynotes.adapter.EmojiAdapter
import com.example.mynotes.databinding.FragmentCreateNotesBinding
import com.example.mynotes.model.Notes
import com.example.mynotes.viewmodel.NotesViewModel
import java.text.SimpleDateFormat
import java.util.*

class CreateNotesFragment : Fragment() {
    lateinit var binding:FragmentCreateNotesBinding
    val viewModel: NotesViewModel by viewModels()
    private var emojiLayoutVisible : Boolean = true
    private var currentEmojiId : Int = 0
    private var isNoSelectedFromDialog = false
    private var isBtnSaveClicked = false


    private lateinit var emojiAdapter: EmojiAdapter
    private var emojiList = mutableListOf<Int>(
        R.drawable.emojiset1_1,
        R.drawable.emojiset1_2,
        R.drawable.emojiset1_3,
        R.drawable.emojiset1_4,
        R.drawable.emojiset1_5,
        R.drawable.emojiset1_6,
        R.drawable.emojiset1_7,
        R.drawable.emojiset1_8,
        R.drawable.emojiset1_9,
        R.drawable.emojiset1_10,
        R.drawable.emojiset1_11,
        R.drawable.emojiset1_12
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding =  FragmentCreateNotesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        setEmojiAdapter()
    }

    private fun setEmojiAdapter() {
        emojiAdapter = EmojiAdapter(emojiList, ::emojiItemClick)
        binding.rvEmojis.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.rvEmojis.adapter = emojiAdapter
    }

    private fun initListeners() {

        binding.btnSaveNotes.setOnClickListener {
            if(binding.edtTitle.text.toString().isNotEmpty() || binding.edtNotes.text.toString().isNotEmpty()) {
                createNotes(it)
                Toast.makeText(requireContext(),"Saved success fully",Toast.LENGTH_SHORT).show()
                isBtnSaveClicked = true
            }
            findNavController().popBackStack()

        }

        binding.ivMood.setOnClickListener {
            if(emojiLayoutVisible) {
                binding.rlMoods.visibility = View.VISIBLE
                binding.viewAlpha.visibility = View.VISIBLE

            }else{
                binding.rlMoods.visibility = View.GONE
                binding.viewAlpha.visibility = View.GONE
            }
            emojiLayoutVisible = !emojiLayoutVisible
        }

        binding.ivClose.setOnClickListener {
            if (binding.edtNotes.text.toString().isNotEmpty() || binding.edtTitle.text.toString()
                    .isNotEmpty()
            ) {
                val alertDialog = AlertDialog.Builder(context)
                alertDialog.setTitle("Diary still not saved")
                alertDialog.setMessage("Do you want to save ?")
                alertDialog.setCancelable(true)
                alertDialog.setPositiveButton("Yes") { _, _ ->
                    createNotes(it)
                    findNavController().popBackStack()
                    isNoSelectedFromDialog = true
                }
                alertDialog.setNegativeButton("No") { _, _ ->
                    isNoSelectedFromDialog = true
                    findNavController().popBackStack()
                }
                alertDialog.show()
            } else {
                findNavController().popBackStack()
            }
        }

        binding.viewAlpha.setOnClickListener {
            binding.rlMoods.visibility = View.GONE
            binding.viewAlpha.visibility = View.GONE
        }
    }

    private fun diaryValidation(): Boolean {
        var status = true

        if(binding.edtNotes.text.isEmpty() && binding.edtTitle.text.isEmpty()){
            status = false
        }else{
            Toast.makeText(requireContext(),"Diary is empty",Toast.LENGTH_SHORT).show()
        }

        return status

    }

    private fun emojiItemClick(emojiId:Int){
        currentEmojiId = emojiId
        when (emojiId) {
            0 -> binding.ivMood.setImageResource(R.drawable.emojiset1_1)
            1 -> binding.ivMood.setImageResource(R.drawable.emojiset1_2)
            2 -> binding.ivMood.setImageResource(R.drawable.emojiset1_3)
            3 -> binding.ivMood.setImageResource(R.drawable.emojiset1_4)
            4 -> binding.ivMood.setImageResource(R.drawable.emojiset1_5)
            5 -> binding.ivMood.setImageResource(R.drawable.emojiset1_6)
            6 -> binding.ivMood.setImageResource(R.drawable.emojiset1_7)
            7 -> binding.ivMood.setImageResource(R.drawable.emojiset1_8)
            8 -> binding.ivMood.setImageResource(R.drawable.emojiset1_9)
            9 -> binding.ivMood.setImageResource(R.drawable.emojiset1_10)
            10 -> binding.ivMood.setImageResource(R.drawable.emojiset1_11)
            11 -> binding.ivMood.setImageResource(R.drawable.emojiset1_12)
        }
    }

    private fun createNotes(it: View?){

        val title = binding.edtTitle.text.toString()
       // val subTitle = binding.edtSubtitle.text.toString()
        val notes = binding.edtNotes.text.toString()
        val id = binding.ivMood.id
        val date = Date()
        val notesDate : CharSequence = DateFormat.format("MMMM d",date.time)

        val data = Notes(
            id = null,title = title,subTitle = "Have to remove this",notes = notes, moodsEmojiId = currentEmojiId ,date = notesDate.toString()
        )

        viewModel.addNotes(data)

        Toast.makeText(requireContext(),"added Successfully",Toast.LENGTH_LONG).show()
    }


    override fun onStop() {
        super.onStop()
        Log.d(Tag, "onStop()")
        if (!isBtnSaveClicked && !isNoSelectedFromDialog && (
                    binding.edtNotes.text.toString()
                        .isNotEmpty() || binding.edtTitle.text.toString()
                        .isNotEmpty())
        ) {
            createNotes(requireView())
        }
    }

}

//val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
//val currentDate = sdf.format(Date())