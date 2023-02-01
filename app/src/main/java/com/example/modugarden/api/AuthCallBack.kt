package com.example.modugarden.api

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.modugarden.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class AuthCallBack<T>(val context: Context, val log: String) : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        Log.d("onResponse", log)
        if(response.code() == 401)
        {
            context.startActivity(Intent(context, LoginActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            )
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {

    }
}