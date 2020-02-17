package com.example.android.sampleproject

import android.app.Application
import io.branch.referral.Branch

class CustomApplicationClass : Application() {

    override fun onCreate() {
        super.onCreate()
//        Branch.enableDebugMode()
//        Branch.getAutoInstance(this)
    }
}
