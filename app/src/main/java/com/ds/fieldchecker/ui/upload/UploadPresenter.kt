package com.ds.fieldchecker.ui.upload

import android.net.Uri
import com.ds.fieldchecker.app.AppController
import com.ds.fieldchecker.base.AbstractBasePresenter
import com.google.firebase.firestore.GeoPoint

class UploadPresenter: AbstractBasePresenter<UploadView>() {

    private var uploadView:UploadView?=null

    override fun setView(view: UploadView) {
        super.setView(view)
        this.uploadView=view
        adminRepo=AppController.getInstanse()?.getAdminRepo()
    }

    fun uploadImage(selectedImageUri: Uri?) {
        uploadView?.showLoading()
        adminRepo?.uploadImagetoBucket(selectedImageUri,this)
    }

    fun updateInDB(locationID: String?, uploadedImg: String?, uploadLocation: GeoPoint) {
        uploadView?.showLoading()
        adminRepo?.updateinDB(this,locationID,uploadedImg,uploadLocation)
    }

    override fun onResponse(responseParser: Any) {
        super.onResponse(responseParser)
        uploadView?.hideLoading()
        when(responseParser){
            is String->{
                uploadView?.onImageUploaded(responseParser)
            }
            is Boolean->{
                uploadView?.onUpdatedDB(responseParser)
            }
        }
    }

    override fun onFailure(message: String) {
        super.onFailure(message)
        uploadView?.let {
            it.hideLoading()
            it.showError(message)
        }
    }
}