package com.example.mynotes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.mynotes.R
import com.example.mynotes.model.Theme

class ThemeAdapter(private val list: MutableList<Theme>, private val vpImageSlider: ViewPager2) : RecyclerView.Adapter<ThemeAdapter.ThemeViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThemeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_theme,parent,false)
        return ThemeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ThemeViewHolder, position: Int) {
        val item = list[position]
        holder.imageView.setImageResource(item.imageTheme)

    }

    class ThemeViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val imageView = itemView.findViewById<ImageView>(R.id.iv_theme)
    }

}