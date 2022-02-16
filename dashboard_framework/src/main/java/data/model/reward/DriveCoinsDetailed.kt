package data.model.reward

import com.google.gson.annotations.SerializedName

internal data class DriveCoinsDetailed(
    @SerializedName("CoinCategoryName") val coinCategoryName: String,
    @SerializedName("CoinCategoryToken") val coinCategoryToken: String,
    @SerializedName("CoinFactor") val coinFactor: String,
    @SerializedName("CoinFactorToken") val coinFactorToken: String,
    @SerializedName("CoinsSum") val coinsSum: Int
)