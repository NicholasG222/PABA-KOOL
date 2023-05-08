package com.example.kool

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class adapterReview (
                     private val listReview : ArrayList<RatingData>
):  RecyclerView.Adapter<adapterReview.ListViewHolder>(){
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvRating = itemView.findViewById<TextView>(R.id.tvRating)
        var tvReview = itemView.findViewById<TextView>(R.id.tvReview)
        var tvEmail = itemView.findViewById<TextView>(R.id.tvEmail)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewreview,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val review = listReview.get(position)
        holder.tvRating.setText(review.rating.toString())
        holder.tvReview.setText(review.review)
        holder.tvEmail.setText(review.email)


    }

    override fun getItemCount(): Int {
        return listReview.size
    }
}