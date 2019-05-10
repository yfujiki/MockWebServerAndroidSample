package com.yfujiki.mockserversample

import android.app.Application
import java.util.concurrent.atomic.AtomicBoolean

class MockWebServerSampleApp: Application() {

    lateinit var service: TMDBService
        private set

    private var _isRunningUITest: AtomicBoolean? = null

    companion object {
        private lateinit var instance: MockWebServerSampleApp

        fun getInstance(): MockWebServerSampleApp {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        if (isRunningUITest() == true) {
            service = ServiceManager.tmdbServiceForUITest()
        } else {
            service = ServiceManager.tmdbService()
        }
    }

    private fun isRunningUITest(): Boolean {
        if (null == _isRunningUITest) {
            var isTest: Boolean

            try {
                Class.forName("androidx.test.espresso.Espresso")
                isTest = true
            } catch (e: ClassNotFoundException) {
                isTest = false
            }

            _isRunningUITest = AtomicBoolean(isTest)
        }

        return _isRunningUITest!!.get()
    }
}

