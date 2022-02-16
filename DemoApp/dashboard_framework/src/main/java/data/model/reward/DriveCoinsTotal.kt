package data.model.reward

import com.google.gson.annotations.SerializedName

internal data class DriveCoinsTotal(
    @SerializedName("TotalEarnedCoins") val totalEarnedCoins: Int,
    @SerializedName("AcquiredCoins") val acquiredCoins: Int
)