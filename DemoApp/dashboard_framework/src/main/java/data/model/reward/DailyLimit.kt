package data.model.reward

import com.google.gson.annotations.SerializedName

internal data class DailyLimit(
    @SerializedName("DailyLimit") val dailyLimit: Int
)