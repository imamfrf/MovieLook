package com.imamfrf.movielook.ui.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imamfrf.movielook.R
import com.imamfrf.movielook.data.model.Cast
import com.imamfrf.movielook.utils.Constants.URL_GET_IMAGE

class CastAdapter(private val listItems: ArrayList<Cast>) :
    RecyclerView.Adapter<CastAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cast, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listItems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]
        holder.textName.text = item.name
        holder.textCharacter.text = item.character
        if (item.photo != "null"){
            Glide.with(holder.itemView.context).load(URL_GET_IMAGE + item.photo)
                .into(holder.imageProfile)
        }
        else{
            holder.imageProfile.setImageResource(R.drawable.stock_profile)
        }


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textName: TextView = itemView.findViewById(R.id.text_cast_name)
        val textCharacter: TextView = itemView.findViewById(R.id.text_cast_character)
        val imageProfile: ImageView = itemView.findViewById(R.id.image_cast)
    }
}