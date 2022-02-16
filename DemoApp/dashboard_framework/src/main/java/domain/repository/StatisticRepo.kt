package domain.repository

import domain.model.leaderboard.LeaderboardMemberData
import domain.model.leaderboard.LeaderboardType
import domain.model.statistics.*

internal interface StatisticRepo {
    suspend fun getDriveCoins(): DriveCoins
    suspend fun getUserStatisticsIndividualData(): UserStatisticsIndividualData
    suspend fun getDrivingDetails(): List<DrivingDetailsData>
    suspend fun getUserStatisticsScoreData(): UserStatisticsScoreData
    suspend fun getMainEcoScoring(): StatisticEcoScoringMain
    suspend fun getEcoScoringStatisticsData(type: Int): StatisticEcoScoringTabData
    suspend fun getLeaderboard(type: LeaderboardType): List<LeaderboardMemberData>?
}