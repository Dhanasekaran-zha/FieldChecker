package com.ds.fieldchecker.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ds.fieldchecker.app.AppController
import com.ds.fieldchecker.databinding.ActivityMainBinding
import com.ds.fieldchecker.ui.dashboard.DashboardActivity
import com.ds.fieldchecker.ui.register.RegisterActivity
import com.ds.github_repo.base.BaseActivity
import com.google.firebase.auth.AuthResult

class LoginActivity : BaseActivity(),LoginView, View.OnClickListener {

    private var binding:ActivityMainBinding?=null
    private val presenter:LoginPresenter= LoginPresenter()

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        presenter.setView(this)

        binding?.registerTxt?.setOnClickListener(this)
        binding?.loginBtn?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            binding?.registerTxt?.id->{
                finishAndStartActiviity(this,RegisterActivity())
            }
            binding?.loginBtn?.id->{
                loginWithEmailPassword()
            }
        }
    }

    fun loginWithEmailPassword(){
        val email=binding?.emailEdt?.text?.trim().toString()
        val password=binding?.passwordEdt?.text?.trim().toString()

        if (email.isNotEmpty() && password.isNotEmpty()){
            presenter.loginWithEmailPassword(email,password)
        } else showError("Enter valid data")
    }

    override fun onLoginComplete(response: AuthResult) {
        showError("Logged In successfully")
        AppController.getInstanse()?.getAppPreference()?.saveUserData(response.user?.email.toString())
        finishAndStartActiviity(this,DashboardActivity())
    }

    override fun showLoading() { showProgress() }

    override fun hideLoading() { hideProgress() }

    override fun showRetry() {}

    override fun hideRetry() {}

    override fun showError(message: String) { Toast.makeText(this, message, Toast.LENGTH_LONG).show() }

    override fun context(): Context = RegisterActivity()

}