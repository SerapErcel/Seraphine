package com.serapercel.seraphine.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.serapercel.seraphine.R
import com.serapercel.seraphine.model.MusicCategory

class CategoryAdapter(private val categoryList: MutableList<MusicCategory>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.tvCategoryName)
        val subCategoryRecyclerView: RecyclerView = itemView.findViewById(R.id.rvSub)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.main_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryWithMusics = categoryList[position]

        holder.titleTextView.text = categoryWithMusics.baseTitle
        holder.subCategoryRecyclerView.layoutManager = LinearLayoutManager(holder.itemView.context)
        categoryWithMusics.items?.let {
            holder.subCategoryRecyclerView.adapter = SubAdapter(categoryWithMusics.items)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}
