package com.ds.fieldchecker.ui.dashboard

import com.ds.fieldchecker.app.AppController
import com.ds.fieldchecker.base.AbstractBasePresenter
import com.ds.fieldchecker.data.repo.model.CoordinateModel

class DashboardPresenter : AbstractBasePresenter<DashboardView>() {

    private var dashboardView: DashboardView? = null

    override fun setView(view: DashboardView) {
        super.setView(view)
        this.dashboardView = view
        adminRepo = AppController.getInstanse()?.getAdminRepo()
    }

    fun signOut() {
        adminRepo?.signOutUser()
    }

    fun getCoordinatesList() {
        dashboardView?.showLoading()
        adminRepo?.getCoordinateList(this)
    }

    override fun onResponse(responseParser: Any) {
        super.onResponse(responseParser)
        dashboardView?.hideLoading()
        when (responseParser) {
            is CoordinateModel -> {
                dashboardView?.onGetCoordinatesList(responseParser)
            }
        }
    }

    override fun onFailure(message: String) {
        super.onFailure(message)
        dashboardView?.let{
            it.hideLoading()
            it.showError(message)
        }
    }
}