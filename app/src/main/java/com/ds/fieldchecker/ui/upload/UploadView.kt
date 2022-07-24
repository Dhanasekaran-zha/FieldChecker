package com.ds.fieldchecker.ui.upload

import com.ds.fieldchecker.base.LoadDataView

interface UploadView:LoadDataView {

    fun onImageUploaded(response: String)
    fun onUpdatedDB(response: Boolean)
}