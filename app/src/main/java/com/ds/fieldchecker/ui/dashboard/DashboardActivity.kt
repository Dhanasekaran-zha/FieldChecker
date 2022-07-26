package com.ds.fieldchecker.ui.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.ds.fieldchecker.app.AppController
import com.ds.fieldchecker.data.repo.model.CoordinateModel
import com.ds.fieldchecker.data.repo.model.CoordinatesItem
import com.ds.fieldchecker.databinding.ActivityDashboardBinding
import com.ds.fieldchecker.ui.dashboard.adapter.CoordinatesAdapter
import com.ds.fieldchecker.ui.login.LoginActivity
import com.ds.fieldchecker.ui.upload.UploadActivity
import com.ds.fieldchecker.utils.MapsUtils
import com.ds.github_repo.base.BaseActivity


class DashboardActivity : BaseActivity(), DashboardView, View.OnClickListener,
    CoordinatesAdapter.coordinateClickListiner {

    private var binding: ActivityDashboardBinding? = null
    private val presenter: DashboardPresenter = DashboardPresenter()
    private var coordinatesAdapter: CoordinatesAdapter? = null
    private lateinit var locationManager:LocationManager
    private lateinit var locationListener: LocationListener
    private var currentLocation:Location?=null


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        presenter.setView(this)
        setContentView(binding?.root)
        locationManager= getSystemService(Context.LOCATION_SERVICE) as LocationManager
        binding?.signOutBtn?.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.getCoordinatesList()
    }
    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding?.signOutBtn?.id -> {
                presenter.signOut()
                AppController.getInstanse()?.getAppPreference()?.signOut()
                finishAndStartActiviity(this, LoginActivity())
            }
        }
    }

    override fun onGetCoordinatesList(response: CoordinateModel) {
        coordinatesAdapter = CoordinatesAdapter(this, this)
        binding?.coordinatesRec?.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = coordinatesAdapter
        }
        coordinatesAdapter?.setList(response.coordinates)
    }

    override fun onCoordinateClicked(coordinatesItem: CoordinatesItem) {
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0F, object :LocationListener{
                override fun onLocationChanged(p0: Location) {
                    if (p0!=null){
                        currentLocation=p0
                        locationManager.removeUpdates(this)
                    }
                }
            })
            if (MapsUtils.isNear(
                    currentLocation,
                    coordinatesItem.taskLocation
                ) || coordinatesItem.status!!
            ) {
                var bundle = Bundle()
                bundle.putParcelable("coordinatesItem", coordinatesItem)
                bundle.putDouble("currentLat", currentLocation?.latitude!!)
                bundle.putDouble("currentLon", currentLocation?.longitude!!)
                startActivity(Intent(this, UploadActivity::class.java).putExtras(bundle))
            } else {
                showError("Goto exact location")
            }
        }else showError("Enable location")
    }

    override fun onLocationClicked(coordinatesItem: CoordinatesItem) {
        val strUri =
            "http://maps.google.com/maps?q=loc:" + coordinatesItem.taskLat.toString() + "," + coordinatesItem.taskLon.toString() + " (" + coordinatesItem.name + ")"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
        startActivity(intent)
    }

    override fun showLoading() { showProgress() }

    override fun hideLoading() { hideProgress() }

    override fun showRetry() {}

    override fun hideRetry() {}

    override fun showError(message: String) { Toast.makeText(this, message, Toast.LENGTH_LONG).show() }

    override fun context(): Context = DashboardActivity()

}