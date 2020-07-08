package com.example.trigentassignment.util

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class Utility(context:Context) {
    private val LOG_TAG="UtilityClass"
    private val context:Context=context
    fun hasActiveInternetConnection(context: Context?): Boolean {
        if (context?.let { isNetworkAvailable(it) }!!) {
            return true;
        } else {
            Log.d(LOG_TAG, "No network available!")
        }
        return false
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
        return activeNetworkInfo != null
    }


}