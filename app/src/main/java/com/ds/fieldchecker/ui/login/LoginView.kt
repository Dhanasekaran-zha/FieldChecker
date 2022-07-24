package com.ds.fieldchecker.ui.login

import com.ds.fieldchecker.base.LoadDataView
import com.google.firebase.auth.AuthResult

interface LoginView:LoadDataView {

    fun onLoginComplete(response: AuthResult)
}