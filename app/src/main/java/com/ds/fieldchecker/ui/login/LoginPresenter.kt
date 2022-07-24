package com.ds.fieldchecker.ui.login

import com.ds.fieldchecker.app.AppController
import com.ds.fieldchecker.base.AbstractBasePresenter
import com.google.firebase.auth.AuthResult

class LoginPresenter:AbstractBasePresenter<LoginView>() {

    private var loginView:LoginView?=null

    override fun setView(view: LoginView) {
        super.setView(view)
        this.loginView=view
        adminRepo=AppController.getInstanse()?.getAdminRepo()
    }

    fun loginWithEmailPassword(email: String, password: String) {
        loginView?.showLoading()
        adminRepo?.loginWithMail(email,password,this)
    }

    override fun onResponse(responseParser: Any) {
        super.onResponse(responseParser)
        loginView?.hideLoading()
        when(responseParser){
            is AuthResult ->{
                val response = responseParser
                loginView?.onLoginComplete(response)
            }
        }
    }

    override fun onFailure(message: String) {
        super.onFailure(message)
        loginView?.let {
            it.hideLoading()
            it.showError(message)
        }
    }
}