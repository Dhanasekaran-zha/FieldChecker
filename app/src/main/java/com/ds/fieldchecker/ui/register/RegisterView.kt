package com.ds.fieldchecker.ui.register

import com.ds.fieldchecker.base.LoadDataView
import com.google.firebase.auth.AuthResult

interface RegisterView:LoadDataView {
    fun onUserCreated(response: AuthResult)
}