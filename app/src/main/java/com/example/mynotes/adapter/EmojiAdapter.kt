package com.example.mynotes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.R
import com.example.mynotes.model.Notes

class EmojiAdapter(private val list: MutableList<Int>,private val onItemClick : (moodEmojiId: Int) -> Unit) : RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmojiViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.emoji_item_layout,parent,false)
        return EmojiViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: EmojiViewHolder, position: Int) {
        val item = list[position]
        holder.imageViewEmoji.setImageResource(item)
        holder.imageViewEmoji.setOnClickListener {
            onItemClick(position)
        }

    }

    class EmojiViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val imageViewEmoji = itemView.findViewById<ImageView>(R.id.iv_emoji_item)
    }
}