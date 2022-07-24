package com.ds.fieldchecker.ui.upload

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.ds.fieldchecker.R
import com.ds.fieldchecker.data.repo.model.CoordinatesItem
import com.ds.fieldchecker.databinding.ActivityUploadBinding
import com.ds.github_repo.base.BaseActivity
import com.google.firebase.firestore.GeoPoint
import java.lang.Exception


class UploadActivity : BaseActivity(),UploadView, View.OnClickListener {

    private var binding:ActivityUploadBinding?=null
    private val presenter:UploadPresenter= UploadPresenter()
    private var coordinatesItem:CoordinatesItem?=null

    private val REQUEST_IMG=101
    var uploadedImg:String?=null
    var uploadedLat:Double?=null
    var uploadedLon:Double?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        presenter.setView(this)

        coordinatesItem=intent.extras?.getParcelable("coordinatesItem")
        uploadedLat=intent.extras?.getDouble("currentLat")
        uploadedLon=intent.extras?.getDouble("currentLon")
        setDataToView(coordinatesItem)

        binding?.locImg?.setOnClickListener(this)
        binding?.submitBtn?.setOnClickListener(this)
    }

    fun setDataToView(coordinatesItem: CoordinatesItem?) {
        binding?.locName?.text= coordinatesItem?.name
        binding?.locLat?.text= coordinatesItem?.taskLat.toString()
        binding?.locLon?.text= coordinatesItem?.taskLon.toString()
        Glide.with(this).load(coordinatesItem?.locationImg).placeholder(R.drawable.ic_photo).into(binding?.locImg!!)
        binding?.locStatus?.text= if (coordinatesItem?.status == true) "Closed" else "Open"
    }

    override fun onClick(p0: View?) {
        if (!coordinatesItem?.status!!){
            when(p0?.id){
                binding?.locImg?.id->{
                    uploadImage()
                }
                binding?.submitBtn?.id->{
                    updateinDB()
                }
            }
        }else showError("Task has been already closed")
    }

    fun uploadImage(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),REQUEST_IMG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == REQUEST_IMG) {
                    val selectedImageUri = intent?.data;
                    presenter.uploadImage(selectedImageUri)
                }
            }
        } catch (e:Exception) {
            Log.e("FileSelectorActivity", "File select error", e);
        }
    }

    fun updateinDB(){
        val uploadLocation=GeoPoint(uploadedLat!!,uploadedLon!!)
        presenter.updateInDB(coordinatesItem?.locationId,uploadedImg,uploadLocation)
    }

    @SuppressLint("ResourceType")
    override fun onImageUploaded(response: String) {
        uploadedImg=response
        Glide.with(this).load(response).into(binding?.locImg!!)
    }

    override fun onUpdatedDB(response: Boolean) {
        if (response) finish()
    }

    override fun showLoading() { showProgress() }

    override fun hideLoading() { hideProgress() }

    override fun showRetry() {}

    override fun hideRetry() {}

    override fun showError(message: String) { Toast.makeText(this, message, Toast.LENGTH_LONG).show() }

    override fun context(): Context = UploadActivity()
}