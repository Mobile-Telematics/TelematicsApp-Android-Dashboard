package data.model.reward

import com.google.gson.annotations.SerializedName

internal data class DriveCoinsDetailed2(
    @SerializedName("AccelerationsCount") val accelerationCount: Int,
    @SerializedName("BrakingsCount") val brakingCount: Int,
    @SerializedName("CorneringsCount") val corneringCount: Int,
    @SerializedName("TotalSpeedingKm") val totalSpeedingKm: Double,
    @SerializedName("PhoneUsageDurationMin") val phoneUsage: Double
)