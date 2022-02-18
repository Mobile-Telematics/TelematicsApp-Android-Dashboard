package com.telematics.dashboard.demoapp

import Credentials
import DashboardModule
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.telematicssdk.auth.TelematicsAuth


class MainActivity : AppCompatActivity() {

    companion object {
        const val instanceId = "your_instance_id"
        const val instanceKey = "your_instance_key"
        const val deviceToken = "your_device_token"
        const val hereKey = "here_key"
    }

    private var accessToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login()
    }

    private fun login() {

        TelematicsAuth.login(
            instanceId,
            instanceKey,
            deviceToken
        )
            .onSuccess {
                accessToken = it.accessToken
                showDashboard()
            }
            .onError {
                Log.d("TelematicsAuth", "login: error ${it.message}")
                showDashboard()
            }
    }

    private fun showDashboard() {

        runOnUiThread {
            val dashboardModule = DashboardModule.getInstance()
            dashboardModule.initialize(this)
            dashboardModule.setHereMapApiKey(hereKey)
            dashboardModule.setCredentials(Credentials(deviceToken, accessToken))
            dashboardModule.setDashboardMileageLimitKm(10)
            showFragment(dashboardModule.getDashboardFragment())
        }
    }

    private fun showFragment(fragment: Fragment) {

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(R.id.main_container, fragment)
        transaction.commit()
    }

}