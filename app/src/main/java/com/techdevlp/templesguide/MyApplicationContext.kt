package com.techdevlp.templesguide

import android.app.Application
import android.content.Context

class MyApplicationContext : Application() {

    companion object {
        private lateinit var instance: MyApplicationContext

        fun getContext(): Context {
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}