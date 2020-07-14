package com.example.trigentassignment.util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.util.Log


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
fun showNetworkDialog(){
    val builder = AlertDialog.Builder(context)
    builder.setTitle("No internet Connection")
    builder.setMessage("Please turn on internet connection")
    builder.setNegativeButton("close",
        DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
    val alertDialog: AlertDialog = builder.create()
    alertDialog.show()
}

}