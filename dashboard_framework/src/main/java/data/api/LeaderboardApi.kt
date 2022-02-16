package data.api

import data.model.rest.ApiResponse
import data.model.statistics.LeaderboardResponse
import data.model.statistics.LeaderboardUserResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

internal interface LeaderboardApi {

    @GET("v1/Leaderboard")
    suspend fun getLeaderBoard(
        @Header("DeviceToken") deviceToken: String,
        @Query("usersCount") userCount: Int? = 10,
        @Query("roundUsersCount") roundUsersCount: Int? = 2,
        @Query("ScoringRate") scoringRate: Int
    ): ApiResponse<LeaderboardResponse>
}