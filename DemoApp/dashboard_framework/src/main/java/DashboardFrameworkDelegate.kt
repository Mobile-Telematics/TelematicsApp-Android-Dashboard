import android.content.Context
import com.dashboard_framework.BuildConfig
import com.google.gson.Gson
import data.api.DriveCoinsApi
import data.api.LeaderboardApi
import data.api.UserStatisticsApi
import data.interceptor.ErrorInterceptor
import data.interceptor.InstanceValuesInterceptor
import data.interceptor.MainInterceptor
import data.model.tracking.TripsMapper
import data.repository.*
import data.tracking.TrackingApiImpl
import data.tracking.TrackingUseCase
import data.utils.ImageLoader
import domain.repository.StatisticRepo
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ui.dashboard.DashboardViewModel

internal class DashboardFrameworkDelegate(context: Context) {

    var dashboardViewModel: DashboardViewModel

    init {
        val sharedPreferences =
            context.getSharedPreferences("dashboard_module_shared_prefs", Context.MODE_PRIVATE)
        val mainInterceptor = MainInterceptor()

        val client = OkHttpClient.Builder().apply {
            addInterceptor(mainInterceptor)
            addInterceptor(InstanceValuesInterceptor())
            authenticator(mainInterceptor)
            addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            addInterceptor(ErrorInterceptor())
        }.build()

        val retrofitDriveCoins = Retrofit
            .Builder()
            .client(client)
            .baseUrl(BuildConfig.driveCoinUrl)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()

        val retrofitUserStatistic = Retrofit
            .Builder()
            .client(client)
            .baseUrl(BuildConfig.userStatisticsUrl)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()

        val retrofitLeaderboard = Retrofit
            .Builder()
            .client(client)
            .baseUrl(BuildConfig.leaderboardUrl)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()

        val driveCoinsApi = retrofitDriveCoins.create(DriveCoinsApi::class.java)
        val userStatisticsApi = retrofitUserStatistic.create(UserStatisticsApi::class.java)

        val statisticRepo: StatisticRepo = StatisticRepoImpl(
            driveCoinsApi,
            userStatisticsApi,
            retrofitLeaderboard.create(LeaderboardApi::class.java)
        )

        val settingsRepo = SettingsRepoImpl(sharedPreferences)
        val measuresFormatter = MeasuresFormatterImpl(settingsRepo)
        val tripsMapper = TripsMapper(measuresFormatter)

        val trackingUseCase = TrackingUseCase(
            context,
            TrackingApiImpl(tripsMapper),
            ImageLoader()
        )

        val rewardRepo = RewardRepoImpl(
            driveCoinsApi,
            userStatisticsApi,
            settingsRepo
        )

        dashboardViewModel = DashboardViewModel(
            statisticRepo,
            trackingUseCase,
            settingsRepo,
            rewardRepo,
            measuresFormatter
        )
    }
}