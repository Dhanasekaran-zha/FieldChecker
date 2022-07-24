package com.ds.fieldchecker.data.repo

import android.net.Uri
import com.ds.fieldchecker.app.ApiInterface
import com.ds.fieldchecker.base.ResponseHandler
import com.ds.fieldchecker.data.repo.model.CoordinateModel
import com.ds.fieldchecker.data.repo.model.CoordinatesItem
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage


class AdminRepo(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseDB: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage
) {

    var storageRef = firebaseStorage.reference

    fun createNewUser(email: String, password: String, handler: ResponseHandler<Any>) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                override fun onComplete(p0: Task<AuthResult>) {
                    try {
                        if (p0.isComplete) {
                            handler.onResponse(p0.result)
                        } else handler.onFailure(p0.exception?.message!!)
                    } catch (e: Exception) {
                        handler.onFailure(p0.exception?.message!!)
                    }
                }
            })
    }

    fun loginWithMail(email: String, password: String, handler: ResponseHandler<Any>) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(object : OnCompleteListener<AuthResult> {
                override fun onComplete(p0: Task<AuthResult>) {
                    try {
                        if (p0.isComplete) {
                            handler.onResponse(p0.result)
                        } else handler.onFailure(p0.exception?.message!!)
                    } catch (e: Exception) {
                        handler.onFailure(p0.exception?.message!!)
                    }
                }
            })
    }

    fun signOutUser() {
        firebaseAuth.signOut()
    }

    fun getCoordinateList(handler: ResponseHandler<Any>) {
        firebaseDB.collection("coordinates").get().addOnSuccessListener { result ->
            if (!result.isEmpty) {
                val response = mapCoordinateList(result.documents)
                handler.onResponse(response)
            } else handler.onFailure("No Data Found")
        }
            .addOnFailureListener { exception ->
                handler.onFailure(
                    exception.message.toString()
                )
            }
    }

    fun mapCoordinateList(documents: MutableList<DocumentSnapshot>): CoordinateModel {

        var coordinateList = arrayListOf<CoordinatesItem>()

        documents.forEach {
            var item = CoordinatesItem()
            item.locationId = it.id
            item.locationImg = it.data?.get("image").toString()
            item.name = it.data?.get("name").toString()
            item.status = it.data?.get("status") as Boolean
            val taskLocation = it.getGeoPoint("location")
            item.taskLocation=taskLocation
            item.taskLat = taskLocation?.latitude
            item.taskLon = taskLocation?.longitude

            coordinateList.add(item)
        }
        val list = CoordinateModel()
        list.coordinates = coordinateList

        return list
    }

    fun uploadImagetoBucket(selectedImageUri: Uri?, handler: ResponseHandler<Any>) {
        val imageRef = storageRef.child("images/${selectedImageUri?.lastPathSegment}")
        val uploadTask = imageRef.putFile(selectedImageUri!!)
        uploadTask.addOnSuccessListener { taskSnapshot ->
            if (taskSnapshot.task.isComplete) {
                if (taskSnapshot.task.isSuccessful) {
                    imageRef.downloadUrl.addOnCompleteListener {
                        if (it.isComplete) {
                            if (it.isSuccessful) {
                                handler.onResponse(it.result.toString())
                            } else handler.onFailure(it.exception?.message!!)
                        } else handler.onFailure(it.exception?.message!!)
                    }
                } else handler.onFailure(taskSnapshot.error?.message!!)
            } else handler.onFailure(taskSnapshot.error?.message!!)
        }.addOnFailureListener {
            handler.onFailure(it.message.toString())
        }
    }

    fun updateinDB(
        handler: ResponseHandler<Any>,
        locationID: String?,
        uploadedImg: String?,
        uploadLocation: GeoPoint
    ) {
        val map = HashMap<String, Any?>()
        map.put("image", uploadedImg);
        map.put("status", true)
        map.put("upload_location", uploadLocation)
        firebaseDB.collection("coordinates").document(locationID!!).update(map)
            .addOnCompleteListener {
                if (it.isComplete) {
                    if (it.isSuccessful) {
                        handler.onResponse(it.isSuccessful)
                    } else handler.onFailure(it.exception?.message!!)
                } else handler.onFailure(it.exception?.message!!)
            }.addOnFailureListener { handler.onFailure(it.message!!) }
    }
}