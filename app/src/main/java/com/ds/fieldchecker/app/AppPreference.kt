package com.ds.fieldchecker.app

import android.content.Context
import android.content.SharedPreferences
import com.ds.fieldchecker.BuildConfig
import com.google.gson.Gson

class AppPreference(val context: Context) {
    private var preference: SharedPreferences? = null

    init {
        preference =
            context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)
    }

    private val USER_MAIL = "USER_MAIL"


    fun saveUserData(mail: String) {
        preference?.edit()?.putString(USER_MAIL, mail)?.apply()
    }

    fun getUserData(): String? {
        return preference?.getString(USER_MAIL,"")
    }

    fun signOut(){
        preference?.edit()?.clear()?.apply()
    }
}
