package data.api

import data.model.rest.ApiResponse
import data.model.reward.DailyLimit
import data.model.reward.DriveCoinsDaily
import data.model.reward.DriveCoinsDetailed
import data.model.reward.DriveCoinsTotal
import data.model.statistics.DriveCoinsRest
import retrofit2.http.GET
import retrofit2.http.Query

internal interface DriveCoinsApi {

    companion object {
        const val API_VERSION_V1 = "v1"
        const val API_VERSION_V2 = "v2"
    }

    @GET("$API_VERSION_V1/Coins/individual")
    suspend fun getDriveCoinsIndividual(
        @Query("DateFrom") startDate: String,
        @Query("DateTo") endDate: String
    ): ApiResponse<DriveCoinsRest>


    /* ---DriveCoins--- */
    @GET("$API_VERSION_V2/DriveCoins/daily")
    suspend fun getDriveCoinsDaily(
        @Query("StartDate") startDate: String,
        @Query("EndDate") endDate: String
    ): ApiResponse<List<DriveCoinsDaily>>

    @GET("$API_VERSION_V2/DriveCoins/dailylimit")
    suspend fun getDriveCoinsDailyLimit(): ApiResponse<DailyLimit>

    @GET("$API_VERSION_V2/DriveCoins/total")
    suspend fun getDriveCoinsTotal(
        @Query("StartDate") startDate: String,
        @Query("EndDate") endDate: String
    ): ApiResponse<DriveCoinsTotal>

    @GET("$API_VERSION_V2/DriveCoins/detailed")
    suspend fun getDriveCoinsDetailed(
        @Query("StartDate") startDate: String,
        @Query("EndDate") endDate: String
    ): ApiResponse<List<DriveCoinsDetailed>>
}