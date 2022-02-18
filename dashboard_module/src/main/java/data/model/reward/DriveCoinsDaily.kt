package data.model.reward

import com.google.gson.annotations.SerializedName

internal data class DriveCoinsDaily(
        @SerializedName("Date") val data: String,
        @SerializedName("DateUpdated") val dateUpdated: String,
        @SerializedName("LimitReached") val limitReached: Boolean,
        @SerializedName("TotalEarnedCoins") val totalEarnedCoins: Int,
        @SerializedName("AcquiredCoins") val acquiredCoins: Int
)