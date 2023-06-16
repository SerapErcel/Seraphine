package com.serapercel.seraphine.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.serapercel.seraphine.R
import com.serapercel.seraphine.model.Music

class SubAdapter(private val subcategoryList: List<Music>) :
    RecyclerView.Adapter<SubAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subCategoryTextView: TextView = itemView.findViewById(R.id.tvSubTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.sub_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = subcategoryList[position]
        holder.subCategoryTextView.text = item.title
        holder.subCategoryTextView.setOnClickListener {
            val bundle = Bundle()
            bundle.putSerializable("music", item)
            Navigation.findNavController(holder.itemView).navigate(R.id.action_homeFragment_to_playFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return subcategoryList.size
    }
}
