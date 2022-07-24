package com.ds.fieldchecker.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.ds.fieldchecker.app.AppController
import com.ds.fieldchecker.databinding.ActivityRegisterBinding
import com.ds.fieldchecker.ui.dashboard.DashboardActivity
import com.ds.github_repo.base.BaseActivity
import com.google.firebase.auth.AuthResult

class RegisterActivity : BaseActivity(), RegisterView, View.OnClickListener {

    private var binding: ActivityRegisterBinding? = null
    private val presenter: RegisterPresenter = RegisterPresenter()

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        presenter.setView(this)

        binding?.registerBtn?.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            binding?.registerBtn?.id->{
                createNewUser()
            }
        }
    }

    private fun createNewUser(){
        val email=binding?.emailEdt?.text?.trim().toString()
        val password=binding?.passwordEdt?.text?.trim().toString()

        if (email.isNotEmpty() && password.isNotEmpty()){
            presenter.createNewUser(email,password)
        } else showError("Enter valid data")
    }

    override fun onUserCreated(response: AuthResult) {
        showError("User Created")
        AppController.getInstanse()?.getAppPreference()?.saveUserData(response.user?.email.toString())
        startActivity(Intent(this,DashboardActivity::class.java))
    }

    override fun showLoading() { showProgress() }

    override fun hideLoading() { hideProgress() }

    override fun showRetry() {}

    override fun hideRetry() {}

    override fun showError(message: String) { Toast.makeText(this, message, Toast.LENGTH_LONG).show() }

    override fun context(): Context = RegisterActivity()

}