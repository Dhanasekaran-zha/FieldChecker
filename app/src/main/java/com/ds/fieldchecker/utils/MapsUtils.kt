package com.ds.fieldchecker.utils

import android.location.Location
import com.google.firebase.firestore.GeoPoint

object MapsUtils {

    fun isNear(currentLocation: Location?, taskLocation: GeoPoint?):Boolean {
        if (currentLocation!=null){
            val distance = FloatArray(1)
            Location.distanceBetween(currentLocation?.latitude!!,currentLocation?.longitude!!,taskLocation?.latitude!!,taskLocation?.longitude!!,distance)
            return distance[0]<1000.0
        }
        return false
    }
}