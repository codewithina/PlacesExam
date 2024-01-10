package com.example.placesexam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.bumptech.glide.Glide

class ListItemAdapter(
    private val places: List<ListItem>,
    private val listener: (ListItem) -> Unit

) : RecyclerView.Adapter<ListItemAdapter.MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val currentPlace = places[position]
        holder.bind(currentPlace, listener)
    }

    override fun getItemCount() = places.size

    inner class MainViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(currentPlace: ListItem, listener: (ListItem) -> Unit) {
            view.findViewById<TextView>(R.id.placeName).text = currentPlace.name
            view.findViewById<TextView>(R.id.placeDescription).text = currentPlace.description

            Glide.with(view.context)
                .load(currentPlace.imageUrl)
                .into(view.findViewById(R.id.ivThumbnail))


            view.setOnClickListener { listener(currentPlace) }
        }
    }

    fun updateData(newData: List<ListItem>) {
        val data = newData
        notifyDataSetChanged()
    }
}