package data.model.statistics

import com.google.gson.annotations.SerializedName

internal data class DriveCoinsRest(
    @SerializedName("TotalCoins") val totalCoins: Int
)