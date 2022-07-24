package com.ds.fieldchecker.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.ds.fieldchecker.app.AppController
import com.ds.fieldchecker.databinding.ActivitySplashBinding
import com.ds.fieldchecker.ui.dashboard.DashboardActivity
import com.ds.fieldchecker.ui.login.LoginActivity
import com.ds.fieldchecker.utils.PermissionHelper
import com.ds.github_repo.base.BaseActivity


class SplashActivity : BaseActivity(), PermissionHelper.PermissionListener {

    private var binding: ActivitySplashBinding? = null
    private var permissionHelper: PermissionHelper? = null


    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        permissionHelper = PermissionHelper(this)

        getPermission()
    }

    fun getPermission(){
        if (permissionHelper!!.isPermissionGranted(PermissionHelper.PermissionType.LOCATION_WITH_STORAGE)) {
            onPermissionGranted()
        } else {
            permissionHelper!!.openPermissionDialog(
                PermissionHelper.PermissionType.LOCATION_WITH_STORAGE,
                this
            )
        }
    }

    fun gotoNextScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            val user = AppController.getInstanse()?.getAppPreference()?.getUserData()

            if (user.isNullOrBlank()) {
                finishAndStartActiviity(this, LoginActivity())
            } else {
                finishAndStartActiviity(this, DashboardActivity())
            }
        }, 1000)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionHelper?.onRequestPermissionsResult(
            requestCode,
            permissions as Array<String>, grantResults
        )
    }

    override fun onPermissionGranted() {
        Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG)
        gotoNextScreen()
    }

    override fun onPermissionDenied() {
        getPermission()

    }
}