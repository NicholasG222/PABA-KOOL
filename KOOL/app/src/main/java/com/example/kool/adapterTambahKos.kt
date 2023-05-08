package com.example.kool

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class adapterTambahKos (
        private val listKost : ArrayList<Kost>
    ) : RecyclerView.Adapter<adapterTambahKos.ListViewHolder>(){
        inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            var _imageKost : ImageView = itemView.findViewById(R.id.imageView)
            var _tNama : TextView = itemView.findViewById(R.id.tNama)
            var _tAlamat : TextView = itemView.findViewById(R.id.tAlamat)
            var _tHarga : TextView = itemView.findViewById(R.id.tHarga)
            var _tRating: TextView = itemView.findViewById(R.id.tRating)
            var buttonLihatPenyewa: Button = itemView.findViewById(R.id.buttonSewaKos)
        }

        private lateinit var onItemClickCallback: OnItemClickCallback

        interface OnItemClickCallback {
            fun onItemClicked(data : Kost)
            fun lihatPenyewa(data: Kost)
        }

        fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
            this.onItemClickCallback = onItemClickCallback
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
            val view : View = LayoutInflater.from(parent.context)
                .inflate(R.layout.viewkost,parent,false)
            return ListViewHolder(view)
        }

        override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
            val kost = listKost.get(position)

            Picasso.get().load(kost.foto!!).into(holder._imageKost)
            holder._tNama.setText(kost.nama)
            holder._tAlamat.setText(kost.alamat)
            holder._tHarga.setText(kost.harga.toString())
            holder._tRating.setText(kost.rating.toString())

            holder._imageKost.setOnClickListener{
                onItemClickCallback.onItemClicked(listKost[position])
            }
            holder.buttonLihatPenyewa.setOnClickListener {
                onItemClickCallback.lihatPenyewa(listKost[position])
            }
        }

        override fun getItemCount(): Int {
            return listKost.size
        }

}