package com.example.kool

import android.os.Parcel
import android.os.Parcelable

data class Kost(
    var pemilik: String?,
    var alamat: String?,
    var fasilitas: String?,
    var foto: String?,
    var harga: Int,
    var nama: String?,
    var telepon: String?,
    var rating: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(pemilik)
        parcel.writeString(alamat)
        parcel.writeString(fasilitas)
        parcel.writeString(foto)
        parcel.writeInt(harga)
        parcel.writeString(nama)
        parcel.writeString(telepon)
        parcel.writeDouble(rating)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Kost> {
        override fun createFromParcel(parcel: Parcel): Kost {
            return Kost(parcel)
        }

        override fun newArray(size: Int): Array<Kost?> {
            return arrayOfNulls(size)
        }
    }
}
