package com.example.seekingbabysitter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seekingbabysitter.R
import com.example.seekingbabysitter.model.Person

class DataAdapter(
    private val dataset: List<Person>
    ): RecyclerView.Adapter<DataAdapter.ItemViewHolder>() {

    private lateinit var mListener : OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        mListener = listener
    }

    class ItemViewHolder(view: View, listener: OnItemClickListener): RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.person_image)
        val nameTextView: TextView = view.findViewById(R.id.full_name)
        val ageTextView: TextView = view.findViewById(R.id.age)
        val cityTextView: TextView = view.findViewById(R.id.city)
        val userIdTextView: TextView = view.findViewById(R.id.user_id)

        init{
            itemView.setOnClickListener{
                listener.onItemClick(absoluteAdapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.material_card_view_design, parent, false)
        return ItemViewHolder(adapterLayout,mListener)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
            holder.imageView.setImageResource(item.user_image)
            holder.nameTextView.text = ("Full Name: " + item.first_name + " " + item.last_name)
            holder.ageTextView.text = ("Age: " + item.age.toString())
            holder.cityTextView.text = ("City: " + item.city)
            holder.userIdTextView.text = ("User Id: " + item.user_id)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}