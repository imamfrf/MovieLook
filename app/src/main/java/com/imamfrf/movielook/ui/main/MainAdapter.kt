package com.imamfrf.movielook.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imamfrf.movielook.R
import com.imamfrf.movielook.data.model.Movie
import com.imamfrf.movielook.utils.Constants.URL_GET_IMAGE

class MainAdapter(private val listItems: ArrayList<Movie>, private val listener: MainListener) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]
        holder.textTitle.text = "${item.title} (${item.releaseDate.substringBefore("-")})"
        Glide.with(holder.itemView.context).load(URL_GET_IMAGE + item.poster)
            .into(holder.imagePoster)
        holder.itemView.setOnClickListener {
            listener.onItemClick(item)
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTitle: TextView = itemView.findViewById(R.id.text_title_card)
        val imagePoster: ImageView = itemView.findViewById(R.id.image_poster_card)
    }

}