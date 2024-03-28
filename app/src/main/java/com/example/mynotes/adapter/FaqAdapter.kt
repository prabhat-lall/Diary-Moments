package com.example.mynotes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.R
import com.example.mynotes.databinding.ItemFaqBinding
import com.example.mynotes.model.Faq

class FaqAdapter(private val faqList : List<Faq>) : RecyclerView.Adapter<FaqAdapter.FaqViewHolder>() {


    private var isQuestionClicked = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        return FaqViewHolder(ItemFaqBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return faqList.size
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        val item = faqList[position]
        holder.binding.tvQuestion.text = item.question
        holder.binding.tvAnswer.text = item.answer
        holder.binding.tvAnswer.visibility = View.GONE
        holder.binding.ivUpDown.setBackgroundResource(R.drawable.baseline_keyboard_arrow_up_24)
        holder.binding.root.setOnClickListener {
            if(isQuestionClicked){
                holder.binding.tvAnswer.visibility = View.VISIBLE
                holder.binding.ivUpDown.setBackgroundResource(R.drawable.baseline_keyboard_arrow_down_24)
                isQuestionClicked = false
            }else{
                holder.binding.tvAnswer.visibility = View.GONE
                holder.binding.ivUpDown.setBackgroundResource(R.drawable.baseline_keyboard_arrow_up_24)
                isQuestionClicked = true

            }
        }
    }

    class FaqViewHolder( val binding : ItemFaqBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}