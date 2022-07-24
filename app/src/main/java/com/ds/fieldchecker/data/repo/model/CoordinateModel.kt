package com.ds.fieldchecker.data.repo.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import com.google.gson.annotations.SerializedName

data class CoordinateModel(

    @field:SerializedName("coordinates")
    var coordinates: ArrayList<CoordinatesItem>? = null
)

data class CoordinatesItem(

    @field:SerializedName("status")
    var status: Boolean? = null,

    @field:SerializedName("name")
    var name: String? = null,

    @field:SerializedName("location")
    var taskLocation: GeoPoint? = null,

    var taskLat: Double? = null,
    var taskLon: Double? = null,
    var locationId: String? = null,
    var locationImg: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        null,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString()
        ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeValue(status)
        p0.writeString(name)
        p0.writeValue(taskLat)
        p0.writeValue(taskLon)
        p0.writeString(locationId)
        p0.writeString(locationImg)
    }

    companion object CREATOR : Parcelable.Creator<CoordinatesItem> {
        override fun createFromParcel(parcel: Parcel): CoordinatesItem {
            return CoordinatesItem(parcel)
        }

        override fun newArray(size: Int): Array<CoordinatesItem?> {
            return arrayOfNulls(size)
        }
    }
}

