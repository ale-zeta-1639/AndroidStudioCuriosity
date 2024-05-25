package com.example.appcuriosity

import android.os.Parcel
import android.os.Parcelable

data class DataClass(var dataTitle:String): Parcelable {

    var isChecked : Boolean = false

    constructor(parcel: Parcel) : this(
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(dataTitle)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataClass> {
        override fun createFromParcel(parcel: Parcel): DataClass {
            return DataClass(parcel)
        }

        override fun newArray(size: Int): Array<DataClass?> {
            return arrayOfNulls(size)
        }
    }
}
