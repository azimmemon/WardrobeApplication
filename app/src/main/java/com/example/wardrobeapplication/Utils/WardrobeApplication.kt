package com.example.wardrobeapplication.Utils

import android.app.Application
import android.content.Context
import org.koin.android.ext.koin.androidContext

class WardrobeApplication: Application() {

    lateinit var mInstance: Context

    override fun onCreate() {
        super.onCreate()
        startModule()
        mInstance = this
    }


    private fun startModule() {
        org.koin.core.context.startKoin {
            androidContext(this@WardrobeApplication)
            modules(Modules.getAll())
        }
    }
}
