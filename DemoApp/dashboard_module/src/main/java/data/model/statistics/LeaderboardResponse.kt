package data.model.statistics

import com.google.gson.annotations.SerializedName

internal data class LeaderboardResponse(
    @SerializedName("ScoringRate")
    val scoringRate: String,
    @SerializedName("Users")
    val users: List<LeaderboardUserBody>,
    @SerializedName("UsersNumber")
    val usersNumber: Int
)