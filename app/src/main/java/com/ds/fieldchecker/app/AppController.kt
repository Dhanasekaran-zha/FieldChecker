package com.ds.fieldchecker.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.ds.fieldchecker.data.repo.AdminRepo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class AppController : Application() {

    companion object {
        var appController: AppController? = null

        fun getInstanse(): AppController? {
            return appController
        }
    }

    private var adminRepo: AdminRepo? = null
    private var gson: Gson? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDB: FirebaseFirestore
    private lateinit var firebaseStorage:FirebaseStorage
    private var preference: AppPreference? = null



    override fun onCreate() {
        super.onCreate()
        appController = this
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
        firebaseAuth = Firebase.auth
        firebaseDB = FirebaseFirestore.getInstance()
        firebaseStorage=Firebase.storage
        adminRepo = getAdminRepo()
        preference = AppPreference(this)
    }


    fun getAdminRepo(): AdminRepo {
        if (adminRepo == null) adminRepo = createAdminRepo()
        return adminRepo!!
    }

    fun getAppPreference(): AppPreference {
        if (preference != null)
            return preference!!

        preference = AppPreference(this)
        return preference!!
    }

    private fun createAdminRepo(): AdminRepo {
        val retrofit: Retrofit
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor { chain ->
                val request: Request = chain.request()

                val newRequest: Request = request.newBuilder()
                    .build()
                return@addInterceptor chain.proceed(newRequest)
            }
            .build()
        retrofit = Retrofit.Builder().client(httpClient)
            .baseUrl(ApiUrl.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()

        return AdminRepo(firebaseAuth,firebaseDB,firebaseStorage)
    }


}