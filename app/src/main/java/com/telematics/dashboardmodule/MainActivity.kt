package com.telematics.dashboardmodule

import Credentials
import DashboardModule
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity() {

    private val deviceToken = "your_device_token"
    private val accessToken = "your_access_token"
    private val hereKey = "here_key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showDashboard(
            deviceToken,
            accessToken,
            hereKey
        )
    }

    private fun showDashboard(deviceToken: String, accessToken: String, hereMapKey: String) {

        val df = DashboardModule.getInstance()
        df.initialize(this)
        df.setHereMapApiKey(hereMapKey)
        val credentials = Credentials(deviceToken, accessToken)
        df.setCredentials(credentials)
        df.setDashboardMileageLimitKm(10)
        val fragment = df.getDashboardFragment()
        showFragment(fragment)
    }

    private fun showFragment(fragment: Fragment) {
        val container = R.id.main_container
        val manager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.replace(container, fragment)
        transaction.commit()
    }
}