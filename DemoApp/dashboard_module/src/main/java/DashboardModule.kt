import android.content.Context
import androidx.fragment.app.Fragment
import data.Globals
import ui.dashboard.DashboardFragment

class DashboardModule private constructor() {

    companion object {
        private lateinit var dashboardModule: DashboardModule
        fun getInstance(): DashboardModule {
            return if (this::dashboardModule.isInitialized)
                dashboardModule
            else {
                dashboardModule = DashboardModule()
                return dashboardModule
            }
        }
    }

    private lateinit var delegate: DashboardModuleDelegate

    fun initialize(context: Context) {
        delegate = DashboardModuleDelegate(context)
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