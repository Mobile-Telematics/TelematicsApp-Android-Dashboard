import android.content.Context
import androidx.fragment.app.Fragment
import data.Globals
import ui.dashboard.DashboardFragment

class DashboardFramework private constructor() {

    companion object {
        private lateinit var dashboardFramework: DashboardFramework
        fun getInstance(): DashboardFramework {
            return if (this::dashboardFramework.isInitialized)
                dashboardFramework
            else {
                dashboardFramework = DashboardFramework()
                return dashboardFramework
            }
        }
    }

    private lateinit var delegate: DashboardFrameworkDelegate

    fun initialize(context: Context) {
        delegate = DashboardFrameworkDelegate(context)
    }

    fun setHereMapApiKey(key: String) {
        Globals.HERE_API_KEY = key
    }

    fun setCredentials(credentials: Credentials) {
        Globals.DEVICE_TOKEN = credentials.deviceToken
        Globals.ACCESS_TOKEN = credentials.accessToken
    }

    fun setDashboardMileageLimitKm(distance: Int) {
        Globals.DASHBOARD_DISTANCE_LIMIT = distance
    }

    fun getDashboardFragment(): Fragment {
        return DashboardFragment(delegate.dashboardViewModel)
    }
}