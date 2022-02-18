package data.model.statistics

import com.google.gson.annotations.SerializedName

internal data class EcoScoringRest(
    @SerializedName("EcoScoreFuel") val fuel: Double,
    @SerializedName("EcoScoreTyres") val tyres: Double,
    @SerializedName("EcoScoreDepreciation") val depreciation: Double,
    @SerializedName("EcoScoreBrakes") val brakes: Double,
    @SerializedName("EcoScore") val score: Double
)