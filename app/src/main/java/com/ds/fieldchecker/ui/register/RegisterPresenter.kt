package com.ds.fieldchecker.ui.register

import com.ds.fieldchecker.app.AppController
import com.ds.fieldchecker.base.AbstractBasePresenter
import com.google.firebase.auth.AuthResult

class RegisterPresenter: AbstractBasePresenter<RegisterView>() {

    private var registerView:RegisterView?=null

    override fun setView(view: RegisterView) {
        super.setView(view)
        this.registerView=view
        adminRepo= AppController.getInstanse()?.getAdminRepo()
    }

    fun createNewUser(email: String, password: String) {
        registerView?.showLoading()
        adminRepo?.createNewUser(email,password,this)
    }

    override fun onResponse(responseParser: Any) {
        super.onResponse(responseParser)
        registerView?.hideLoading()
        when(responseParser){
            is AuthResult ->{
                val response = responseParser
                registerView?.onUserCreated(response)
            }
        }
    }

    override fun onFailure(message: String) {
        super.onFailure(message)
        registerView?.let {
            it.hideLoading()
            it.showError(message)
        }
    }
}