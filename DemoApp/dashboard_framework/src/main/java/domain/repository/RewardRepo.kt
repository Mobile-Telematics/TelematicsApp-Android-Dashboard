package domain.repository

import domain.model.reward.*

internal interface RewardRepo {

    suspend fun getDaily(): List<Pair<Int, Int>>
    suspend fun getDailyLimit(): DailyLimitData
    suspend fun getTotalCoinsByDuration(duration: DriveCoinsDuration): DriveCoinsTotalData
    suspend fun getDetailed(duration: DriveCoinsDuration): DriveCoinsDetailedData
    suspend fun getStreaks(): List<Streak>
    suspend fun getDrivingStreaks(): StreaksData
}