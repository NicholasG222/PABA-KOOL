package com.example.kool

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.collection.LLRBNode
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import java.text.DecimalFormat

class adapterKost (
    private val listKost : ArrayList<Kost>, val context: Context
    ) : RecyclerView.Adapter<adapterKost.ListViewHolder>(){
        inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            var _imageKost : ImageView = itemView.findViewById(R.id.imageView)
            var _tNama : TextView = itemView.findViewById(R.id.tNama)
            var _tAlamat : TextView = itemView.findViewById(R.id.tAlamat)
            var _tHarga : TextView = itemView.findViewById(R.id.tHarga)
            var _tRating: TextView = itemView.findViewById(R.id.tRating)
            var buttonSewa: Button = itemView.findViewById(R.id.buttonSewa)
            var buttonReview: Button = itemView.findViewById(R.id.buttonLihatReview)

        }

    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var sp: SharedPreferences

    interface OnItemClickCallback {
        fun onItemClicked(data : Kost)
        fun pindahHal(data: Kost)
        fun pindahPay(data: Kost)
        fun berhentiSewa(data: Kost)

    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewkostcustomer,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val kost = listKost.get(position)
        sp = context.getSharedPreferences("data_SP", AppCompatActivity.MODE_PRIVATE)
        val db = Firebase.firestore
        db.collection("tbUser").get().addOnSuccessListener { document ->
            for(result in document) {
                var contains = false
                val isi = sp.getString("SPlogin", null)
                if (isi == result.getString("email")) {
                    val kostSewa = result.get("kostSewa") as MutableList<Any?>
                    for (i in kostSewa) {
                        if ((i as HashMap<String, Any>).get("foto") == kost.foto) {
                            contains = true
                        }
                    }
                    if (contains.not()) {
                        holder.buttonSewa.setBackgroundColor(Color.BLUE)
                        holder.buttonSewa.setText("Sewa \n kos")
                        holder.buttonSewa.setOnClickListener {
                            onItemClickCallback.pindahPay(kost)
                        }
                    } else {
                        holder.buttonSewa.setBackgroundColor(Color.RED)
                        holder.buttonSewa.setText("Berhenti \n sewa")
                        holder.buttonSewa.setOnClickListener {
                            AlertDialog.Builder(context).setTitle(
                                "Berhenti sewa"
                            ).setMessage("Apakah Anda yakin ingin berhenti menyewa " + kost.nama +"?").setPositiveButton(
                                "BERHENTI SEWA", DialogInterface.OnClickListener {
                                    dialog, which ->
                                    onItemClickCallback.berhentiSewa(kost)
                                }
                            ).setNegativeButton("BATAL", DialogInterface.OnClickListener { dialog, which ->
                                Toast.makeText(context, "DATA BATAL DIHAPUS", Toast.LENGTH_LONG).show()
                            }).show()

                        }
                    }
                }
            }
        }
        Picasso.get().load(kost.foto!!).into(holder._imageKost)
        holder._tNama.setText(kost.nama)
        holder._tAlamat.setText(kost.alamat)
        val df = DecimalFormat("#.#")
        holder._tHarga.setText(kost.harga.toString())
        holder._tRating.setText(df.format(kost.rating).toString())

        holder._imageKost.setOnClickListener{
            onItemClickCallback.onItemClicked(listKost[position])
        }

        holder.buttonReview.setOnClickListener {
            onItemClickCallback.pindahHal(listKost[position])
        }


    }

    override fun getItemCount(): Int {
        return listKost.size
    }

}