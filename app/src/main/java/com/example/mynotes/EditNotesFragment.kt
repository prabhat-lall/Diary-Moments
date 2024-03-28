package com.example.mynotes

import android.app.AlertDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mynotes.adapter.EmojiAdapter
import com.example.mynotes.databinding.FragmentEditNotesBinding
import com.example.mynotes.model.Notes
import com.example.mynotes.viewmodel.NotesViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.Date

const val NOTES_DATA = "NOTES_DATA"
const val Tag = "EditNotesFragment"

class EditNotesFragment : Fragment() {

    // val oldNotes by navArgs<EditNotesFragmentArgs>()
    private var oldNotes: Notes? = null
    private lateinit var binding: FragmentEditNotesBinding
    private val viewModel: NotesViewModel by viewModels()
    private var emojiLayoutVisible: Boolean = true
    private var currentEmojiId: Int = 0
    private var isNoSelectedFromDialog = false

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            oldNotes = it.getBundle(NOTES_DATA)
//        }
        val args = requireArguments()
        oldNotes = args.getParcelable("data")
            ?: throw IllegalArgumentException("Argument 'data' is missing or has incorrect type")

        Log.d(Tag, "onCreate()")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditNotesBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)
        Log.d(Tag, "onCreateView()")
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setStatusBarColor()
        //requireActivity()?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        //activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        binding.edtTitle.setText(oldNotes?.title)
        // binding.edtSubtitle.setText(oldNotes?.subTitle)
        binding.edtNotes.setText(oldNotes?.notes)
        emojiItemClick(oldNotes?.moodsEmojiId ?: 0)

        initListeners()
        emojiAdapter = EmojiAdapter(emojiList, ::emojiItemClick)
        binding.rvEmojis.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.rvEmojis.adapter = emojiAdapter

        Log.d(Tag, "onViewCreated()")
        val buttonBottomMargin = binding.btnSaveNotes.layoutParams as ViewGroup.MarginLayoutParams

        // Get the root view of your layout

    }


    private fun emojiItemClick(emojiId: Int) {
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


    private fun initListeners() {
        binding.btnSaveNotes.setOnClickListener {
            if(binding.edtTitle.text.toString().isNotEmpty() || binding.edtNotes.text.toString().isNotEmpty()) {
                updateNotes(it)
                Toast.makeText(requireContext(),"Saved success fully",Toast.LENGTH_SHORT).show()
            }
            findNavController().popBackStack()
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
                    updateNotes(it)
                    findNavController().popBackStack()
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

        //todo Prabhat have to removebelow code below code is to create pdf and launch
//            val text = binding.edtNotes.text.toString()
//            val pdfFile = Utility().createStyledPdf(requireContext(), text)
//            Log.d("_prabhat", "initListeners:${pdfFile.toString()} ")
//            if (pdfFile != null) {
//                Toast.makeText(requireContext(),"Pdf file created successfully",Toast.LENGTH_SHORT).show()
//                Utility().openPdfFileUsingFilePath(requireContext(),pdfFile)
//                Log.d("_prabhat", "initListeners:${pdfFile.toString()} ")
//            } else {
//                Toast.makeText(requireContext(),"Some thing went wrong",Toast.LENGTH_SHORT).show()
//            }

        binding.ivMood.setOnClickListener {
            if (emojiLayoutVisible) {
                binding.rlMoods.visibility = View.VISIBLE
                binding.viewAlpha.visibility = View.VISIBLE
            } else {
                binding.rlMoods.visibility = View.GONE
                binding.viewAlpha.visibility = View.GONE
            }
            emojiLayoutVisible = !emojiLayoutVisible
        }

        binding.viewAlpha.setOnClickListener {
            binding.rlMoods.visibility = View.GONE
            binding.viewAlpha.visibility = View.GONE
        }

    }

    override fun onDetach() {
        super.onDetach()
        Log.d(Tag, "onDetach()")

        // requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun onStart() {
        super.onStart()
        Log.d(Tag, "onStart()")

    }

    override fun onPause() {
        super.onPause()
        Log.d(Tag, "onPause()")

    }

    override fun onResume() {
        super.onResume()
        Log.d(Tag, "onResume()")

    }

    override fun onStop() {
        super.onStop()
        Log.d(Tag, "onStop()")
        if (!isNoSelectedFromDialog && (binding.edtNotes.text.toString().isNotEmpty() || binding.edtTitle.text.toString()
                .isNotEmpty() )
        ) {
            updateNotes(requireView())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(Tag, "onDestroyView()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(Tag, "onDestroy()")

    }
    private fun updateNotes(it: View?) {
        val title = binding.edtTitle.text.toString()
        //  val subTitle = binding.edtSubtitle.text.toString()
        val notes = binding.edtNotes.text.toString()
        val id = binding.ivMood.id

        val date = Date()
        val notesDate: CharSequence = DateFormat.format("MMMM d", date.time)

        val data = Notes(
            id = oldNotes?.id,
            title = title,
            subTitle = "Have to remove",
            notes = notes,
            moodsEmojiId = currentEmojiId,
            date = notesDate.toString()
        )
        viewModel.updateNotes(data)
        Toast.makeText(requireContext(), "notes updated Successfully", Toast.LENGTH_SHORT).show()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_sort) {
            val bottomSheet: BottomSheetDialog = BottomSheetDialog(requireContext())
            bottomSheet.setContentView(R.layout.dialog_delete)
            val buttonYes = bottomSheet.findViewById<Button>(R.id.yes_dialog)
            val buttonNo = bottomSheet.findViewById<Button>(R.id.no_dialog)

            buttonYes?.setOnClickListener {
                viewModel.deleteNotes(oldNotes?.id!!)
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

    private fun setStatusBarColor() {
        val window: Window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.background_100)
    }

    companion object {

        @JvmStatic
        fun newInstance(data: Notes) =
            EditNotesFragment().apply {
                arguments = Bundle().apply {
                    oldNotes = data
                }
            }
    }

}