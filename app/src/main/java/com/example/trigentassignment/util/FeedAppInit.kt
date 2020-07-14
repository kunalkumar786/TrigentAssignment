package com.example.trigentassignment.util

import android.app.Application
import com.anupcowkur.reservoir.Reservoir

class FeedAppInit : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            Reservoir.init(this, 1000000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
