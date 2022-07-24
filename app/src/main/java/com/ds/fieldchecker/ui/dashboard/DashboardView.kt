package com.ds.fieldchecker.ui.dashboard

import com.ds.fieldchecker.base.LoadDataView
import com.ds.fieldchecker.data.repo.model.CoordinateModel

interface DashboardView :LoadDataView {
    fun onGetCoordinatesList(responseParser: CoordinateModel)
}