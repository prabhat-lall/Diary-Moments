package com.example.mynotes.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.HomeFragmentDirections
import com.example.mynotes.R
import com.example.mynotes.databinding.ItemNotesBinding
import com.example.mynotes.model.Notes


class NotesAdapter(
    private val requireContext: Context,
    private var notesList: List<Notes>,
    private val showMenuDelete: (Boolean) -> Unit,
    private val onItemClick : (data:Notes) -> Unit,
) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    //private var isEnabled = false
    val itemSelectedList = mutableListOf<Int>()
    fun filterNotes(newFilteredList: ArrayList<Notes>) {
        notesList = newFilteredList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            ItemNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        Log.d("_prabaht", "onBindViewHolder insider: $position")
        val data = notesList[position]
        holder.binding.notesName.text = data.title
        holder.binding.notesDescription.text = data.notes
        holder.binding.notesDate.text = data.date
        emojiItemClick(holder.binding.ivMoodsEmoji,data.moodsEmojiId)

        // Set background color based on item selection
        if (itemSelectedList.contains(data.id!!)) {
            holder.binding.clItemNotes.setBackgroundResource(R.color.statusBarColor)
        } else {
            holder.binding.clItemNotes.setBackgroundResource(R.color.bg_item_notes)
        }

        holder.binding.cvNotesItem.setOnClickListener {
            if(itemSelectedList.size == 0) {
                showMenuDelete(false)
                onItemClick(data)
            } else if(itemSelectedList.contains(data.id!!)) {
                itemSelectedList.remove(data.id!!)
                if(itemSelectedList.size == 0) {
                    showMenuDelete(false)
                }
                Log.d("NotesAdapter","${data.id}")
                notifyDataSetChanged() // Notify data set changed to update the view
            } else if(!itemSelectedList.contains(data.id!!)) {
                itemSelectedList.add(data.id!!)
                Log.d("NotesAdapter","click")
                notifyDataSetChanged() // Notify data set changed to update the view
            }
        }

        holder.binding.cvNotesItem.setOnLongClickListener {
            holder.binding.clItemNotes.setBackgroundResource(R.color.statusBarColor)
            itemSelectedList.add(data.id!!)
            showMenuDelete(true)
            Log.d("NotesAdapter","Long click")
            notifyDataSetChanged() // Notify data set changed to update the view
            true
        }
    }



    private fun selectItem(holder: NotesViewHolder, data: Notes, position: Int) {
        // holder.binding.cvNotesItem.setCardBackgroundColor(R.color.white)
        holder.binding.clItemNotes.setBackgroundResource(R.color.statusBarColor)
        //isEnabled = true
        itemSelectedList.add(data.id!!)
        Log.d("@@@@@","${data.id}")
        showMenuDelete(true)
    }

    private fun emojiItemClick(view: ImageView, emojiId:Int){
        when (emojiId) {
            0 -> view.setImageResource(R.drawable.emojiset1_1)
            1 -> view.setImageResource(R.drawable.emojiset1_2)
            2 -> view.setImageResource(R.drawable.emojiset1_3)
            3 -> view.setImageResource(R.drawable.emojiset1_4)
            4 -> view.setImageResource(R.drawable.emojiset1_5)
            5 -> view.setImageResource(R.drawable.emojiset1_6)
            6 -> view.setImageResource(R.drawable.emojiset1_7)
            7 -> view.setImageResource(R.drawable.emojiset1_8)
            8 -> view.setImageResource(R.drawable.emojiset1_9)
            9 -> view.setImageResource(R.drawable.emojiset1_10)
            10 -> view.setImageResource(R.drawable.emojiset1_11)
            11 -> view.setImageResource(R.drawable.emojiset1_12)
        }
    }

    class NotesViewHolder(val binding: ItemNotesBinding) : RecyclerView.ViewHolder(binding.root) {

    }

}

/**
class NotesAdapter(
    private val requireContext: Context,
    private var notesList: List<Notes>,
    private val showMenuDelete: (Boolean) -> Unit,
    private val onItemClick : (data:Notes) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    //private var isEnabled = false
    val itemSelectedList = mutableListOf<Int>()
    fun filterNotes(newFilteredList: ArrayList<Notes>) {
        notesList = newFilteredList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            ItemNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        Log.d("_prabaht", "onBindViewHolder insider: $position")
        val data = notesList[position]
        holder.binding.notesName.text = data.title
        holder.binding.notesDescription.text = data.notes
        holder.binding.notesDate.text = data.date
        emojiItemClick(holder.binding.ivMoodsEmoji,data.moodsEmojiId)
        holder.binding.cvNotesItem.setOnClickListener {
            if(itemSelectedList.size == 0) {
                showMenuDelete(false)
                onItemClick(data)
            }else if(itemSelectedList.contains(data.id!!)){
                itemSelectedList.remove(data.id!!)
                if(itemSelectedList.size == 0){
                    showMenuDelete(false)
                }
                Log.d("NotesAdapter","${data.id}")
                holder.binding.clItemNotes.setBackgroundResource(R.color.bg_item_notes)
            }else if(!itemSelectedList.contains(data.id!!)){
                itemSelectedList.add(data.id!!)
                Log.d("NotesAdapter","click")
                holder.binding.clItemNotes.setBackgroundResource(R.color.statusBarColor)
            }

        }
        holder.binding.cvNotesItem.setOnLongClickListener {
            holder.binding.clItemNotes.setBackgroundResource(R.color.statusBarColor)
            itemSelectedList.add(data.id!!)
            showMenuDelete(true)
            Log.d("NotesAdapter","Long click")
            //selectItem(holder, data, position)
            true
        }

    }


    private fun selectItem(holder: NotesViewHolder, data: Notes, position: Int) {
       // holder.binding.cvNotesItem.setCardBackgroundColor(R.color.white)
        holder.binding.clItemNotes.setBackgroundResource(R.color.statusBarColor)
        //isEnabled = true
        itemSelectedList.add(data.id!!)
        Log.d("@@@@@","${data.id}")
        showMenuDelete(true)
    }

    private fun emojiItemClick(view: ImageView, emojiId:Int){
        when (emojiId) {
            0 -> view.setImageResource(R.drawable.emojiset1_1)
            1 -> view.setImageResource(R.drawable.emojiset1_2)
            2 -> view.setImageResource(R.drawable.emojiset1_3)
            3 -> view.setImageResource(R.drawable.emojiset1_4)
            4 -> view.setImageResource(R.drawable.emojiset1_5)
            5 -> view.setImageResource(R.drawable.emojiset1_6)
            6 -> view.setImageResource(R.drawable.emojiset1_7)
            7 -> view.setImageResource(R.drawable.emojiset1_8)
            8 -> view.setImageResource(R.drawable.emojiset1_9)
            9 -> view.setImageResource(R.drawable.emojiset1_10)
            10 -> view.setImageResource(R.drawable.emojiset1_11)
            11 -> view.setImageResource(R.drawable.emojiset1_12)
        }
    }

    class NotesViewHolder(val binding: ItemNotesBinding) : RecyclerView.ViewHolder(binding.root) {

    }

} */