package ui.dashboard

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.extentions.setLiveData
import data.extentions.setLiveDataForResult
import data.mappers.toScoreTypeModelList
import data.model.tracking.MeasuresFormatter
import data.tracking.TrackingUseCase
import data.utils.Resource
import domain.model.leaderboard.LeaderboardType
import domain.model.reward.StreaksData
import domain.model.statistics.*
import domain.model.tracking.TripData
import domain.repository.RewardRepo
import domain.repository.SettingsRepo
import domain.repository.StatisticRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import java.util.*

internal class DashboardViewModel constructor(
    private val statisticRepo: StatisticRepo,
    private val trackingUseCase: TrackingUseCase,
    private val settingsRepo: SettingsRepo,
    private val rewardRepo: RewardRepo,
    val measuresFormatter: MeasuresFormatter
) : ViewModel() {

    private val TAG = "DashboardViewModel"

    val driveCoinsData = MutableLiveData<Resource<DriveCoins>>()
    val userIndividualStatisticsData = MutableLiveData<Resource<UserStatisticsIndividualData>>()
    val scoreLiveData = MutableLiveData<Resource<StatisticScoringData>>()
    val mainEcoScoringLiveData = MutableLiveData<Resource<StatisticEcoScoringMain>>()
    val tableEcoScoringLiveData = MutableLiveData<Resource<StatisticEcoScoringTabsData>>()

    fun getDriveCoins() {

        flow {
            emit(statisticRepo.getDriveCoins())
        }
            .flowOn(Dispatchers.IO)
            .setLiveData(driveCoinsData)
            .launchIn(viewModelScope)
    }

    fun getUserIndividualStatistics() {

        flow {
            val data = statisticRepo.getUserStatisticsIndividualData()
            emit(data)
        }
            .flowOn(Dispatchers.IO)
            .setLiveData(userIndividualStatisticsData)
            .launchIn(viewModelScope)
    }

    fun getStatistics() {

        flow {
            val list = statisticRepo.getDrivingDetails()
            val userStatisticsIndividualData = statisticRepo.getUserStatisticsIndividualData()
            val userStatisticsScoreData = statisticRepo.getUserStatisticsScoreData()
            val scoreTypeModelChart = list.toScoreTypeModelList()
            val scoreTypeModelNumbers = userStatisticsScoreData.toScoreTypeModelList()
            val scoreData = StatisticScoringData(
                scoreTypeModelChart,
                userStatisticsIndividualData,
                scoreTypeModelNumbers
            )
            emit(scoreData)
        }
            .flowOn(Dispatchers.IO)
            .setLiveData(scoreLiveData)
            .launchIn(viewModelScope)


    }

    fun getMainEcoScoring() {

        flow {
            emit(statisticRepo.getMainEcoScoring())
        }
            .flowOn(Dispatchers.IO)
            .setLiveData(mainEcoScoringLiveData)
            .launchIn(viewModelScope)
    }

    fun getEcoScoringTable() {

        flow {
            val weekData = statisticRepo.getEcoScoringStatisticsData(Calendar.DAY_OF_WEEK)
            val monthData = statisticRepo.getEcoScoringStatisticsData(Calendar.MONTH)
            val yearData = statisticRepo.getEcoScoringStatisticsData(Calendar.YEAR)
            emit(StatisticEcoScoringTabsData(weekData, monthData, yearData))
        }
            .flowOn(Dispatchers.IO)
            .setLiveData(tableEcoScoringLiveData)
            .launchIn(viewModelScope)
    }


    fun getLastTrip(): LiveData<Result<TripData?>> {

        val lastTripDataState = MutableLiveData<Result<TripData?>>()
        trackingUseCase.getLastTrip()
            .flowOn(Dispatchers.IO)
            .setLiveDataForResult(lastTripDataState)
            .launchIn(viewModelScope)
        return lastTripDataState
    }

    fun getLastTripImage(token: String): LiveData<Result<Bitmap?>> {

        val lastTripImageState = MutableLiveData<Result<Bitmap?>>()
        trackingUseCase.getTripImage(token)
            .flowOn(Dispatchers.IO)
            .setLiveDataForResult(lastTripImageState)
            .launchIn(viewModelScope)
        return lastTripImageState
    }

    fun getTelematicsLink(context: Context): String {
        return settingsRepo.getTelematicsLink(context)
    }

    fun getRank(): LiveData<Result<Int>> {

        val rankState = MutableLiveData<Result<Int>>()
        flow {
            val data =
                statisticRepo.getLeaderboard(LeaderboardType.Rate)
                    ?.find { it.isCurrentUser }?.rank ?: 1
            emit(data)
        }
            .flowOn(Dispatchers.IO)
            .setLiveDataForResult(rankState)
            .launchIn(viewModelScope)
        return rankState
    }

    fun getDrivingStreaks(): LiveData<Result<StreaksData>> {

        val drivingStreakState = MutableLiveData<Result<StreaksData>>()
        flow {
            val data = rewardRepo.getDrivingStreaks()
            emit(data)
        }
            .flowOn(Dispatchers.IO)
            .setLiveDataForResult(drivingStreakState)
            .launchIn(viewModelScope)
        return drivingStreakState
    }

    fun getFormatterDate(dateInString: String): String {

        val date = measuresFormatter.parseFullNewDate(dateInString)
        return measuresFormatter.getDateWithTime(date)
    }
}