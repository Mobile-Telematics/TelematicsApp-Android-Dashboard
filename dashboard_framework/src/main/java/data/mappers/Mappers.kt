package data.mappers

import com.dashboard_framework.R
import data.model.reward.*
import data.model.statistics.*
import domain.model.leaderboard.LeaderboardMemberData
import domain.model.leaderboard.LeaderboardType
import domain.model.leaderboard.LeaderboardUser
import domain.model.leaderboard.LeaderboardUserItems
import domain.model.measures.DateMeasure
import domain.model.reward.*
import domain.model.statistics.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

internal fun UserStatisticsIndividualRest.toUserStatisticsIndividualData(): UserStatisticsIndividualData {
    return UserStatisticsIndividualData(
        this.tripsCount.roundToInt(),
        this.mileageKm,
        this.mileageMile,
        this.drivingTime.roundToInt()
    )
}

internal fun DrivingDetailsRest.toDrivingDetailsData(): DrivingDetailsData {

    return DrivingDetailsData(
        "",
        .0,
        .0,
        0,
        this.score,
        this.scoreAcceleration,
        this.scoreDate,
        this.scoreDeceleration,
        this.scoreDistraction,
        this.scoreSpeeding,
        this.scoreTurn,
        0
    )
}

internal fun UserStatisticsScoreRest.toUserStatisticsScoreData(): UserStatisticsScoreData {
    return UserStatisticsScoreData(
        this.overallScore.toInt(),
        this.accelerationScore.toInt(),
        this.brakingScore.toInt(),
        this.distractedScore.toInt(),
        this.speedingScore.toInt(),
        this.corneringScore.toInt()
    )
}

internal fun List<DrivingDetailsData>.toScoreTypeModelList(): List<ScoreTypeModel> {

    val data = this

    val res = mutableListOf(
        ScoreTypeModel(ScoreType.OVERALL, data.last().score, mutableListOf()),
        ScoreTypeModel(ScoreType.ACCELERATION, data.last().scoreAcceleration, mutableListOf()),
        ScoreTypeModel(ScoreType.BREAKING, data.last().scoreDeceleration, mutableListOf()),
        ScoreTypeModel(ScoreType.PHONE_USAGE, data.last().scoreDistraction, mutableListOf()),
        ScoreTypeModel(ScoreType.SPEEDING, data.last().scoreSpeeding, mutableListOf()),
        ScoreTypeModel(ScoreType.CORNERING, data.last().scoreTurn, mutableListOf())
    )
    val mins: Array<Int?> = Array(res.size) { null }
    val indexes: Array<Int?> = Array(res.size) { null }
    for (i in data.indices) {
        val index = data[i].scoreDate.indexOf("T")
        val date = if (index >= 0) data[i].scoreDate.substring(index - 2, index).toInt() else i

        res[0].data.add(Pair(date, data[i].score))
        if (data[i].score != 100 && data[i].score > 20) {
            if (mins[0] == null || mins[0]!! > data[i].score) {
                mins[0] = data[i].score
                indexes[0] = i
            }
        }

        res[1].data.add(Pair(date, data[i].scoreAcceleration))
        if (data[i].scoreAcceleration != 100 && data[i].scoreAcceleration > 20) {
            if (mins[1] == null || mins[1]!! > data[i].scoreAcceleration) {
                mins[1] = data[i].scoreAcceleration
                indexes[1] = i
            }
        }

        res[2].data.add(Pair(date, data[i].scoreDeceleration))
        if (data[i].scoreDeceleration != 100 && data[i].scoreDeceleration > 20) {
            if (mins[2] == null || mins[2]!! > data[i].scoreDeceleration) {
                mins[2] = data[i].scoreDeceleration
                indexes[2] = i
            }
        }

        res[3].data.add(Pair(date, data[i].scoreDistraction))
        if (data[i].scoreDistraction != 100 && data[i].scoreDistraction > 20) {
            if (mins[3] == null || mins[3]!! > data[i].scoreDistraction) {
                mins[3] = data[i].scoreDistraction
                indexes[3] = i
            }
        }
        res[4].data.add(Pair(date, data[i].scoreSpeeding))
        if (data[i].scoreSpeeding != 100 && data[i].scoreSpeeding > 20) {
            if (mins[4] == null || mins[4]!! > data[i].scoreSpeeding) {
                mins[4] = data[i].scoreSpeeding
                indexes[4] = i
            }
        }
        res[5].data.add(Pair(date, data[i].scoreTurn))
        if (data[i].scoreTurn != 100 && data[i].scoreTurn > 20) {
            if (mins[5] == null || mins[5]!! > data[i].scoreTurn) {
                mins[5] = data[i].scoreTurn
                indexes[5] = i
            }
        }

    }

    for (i in indexes.indices) {
        indexes[i]?.let {
            res[i].data[it] = res[i].data[it].copy(res[i].data[it].first, res[i].data[it].second)
        }
    }

    return res
}

internal fun EcoScoringRest.toDashboardEcoScoringMain(): StatisticEcoScoringMain {
    val response = this
    return StatisticEcoScoringMain(
        score = response.score.toInt(),
        fuel = response.fuel.toInt(),
        brakes = response.brakes.toInt(),
        tires = response.tyres.toInt(),
        cost = response.depreciation.toInt()
    )
}

internal fun UserStatisticsIndividualRest.toDashboardEcoScoringTabData(): StatisticEcoScoringTabData {
    val response = this
    val averageTripDistance = (response.mileageKm) / (response.tripsCount)
    return StatisticEcoScoringTabData(
        response.averageSpeedKmh,
        response.maxSpeedKmh,
        averageTripDistance
    )
}

internal fun UserStatisticsScoreData.toScoreTypeModelList(): List<ScoreTypeModel> {
    return listOf(
        ScoreTypeModel(ScoreType.OVERALL, overallScore),
        ScoreTypeModel(ScoreType.ACCELERATION, accelerationScore),
        ScoreTypeModel(ScoreType.BREAKING, brakingScore),
        ScoreTypeModel(ScoreType.PHONE_USAGE, distractedScore),
        ScoreTypeModel(ScoreType.SPEEDING, speedingScore),
        ScoreTypeModel(ScoreType.CORNERING, corneringScore)
    )
}

internal fun LeaderboardResponse.toLeaderboardData(type: Int): List<LeaderboardMemberData> {

    val mappedType = when (type) {
        1 -> LeaderboardType.Acceleration
        2 -> LeaderboardType.Deceleration
        3 -> LeaderboardType.Distraction
        4 -> LeaderboardType.Speeding
        5 -> LeaderboardType.Turn
        6 -> LeaderboardType.Rate
        7 -> LeaderboardType.Distance
        8 -> LeaderboardType.Trips
        9 -> LeaderboardType.Duration
        else -> LeaderboardType.Rate
    }

    val listLeaderboardMemberRest = this.users

    if (listLeaderboardMemberRest.isNullOrEmpty()) return emptyList()

    val listFriendsMembersData = ArrayList<LeaderboardMemberData>(listLeaderboardMemberRest.size)
    listLeaderboardMemberRest.indices.mapTo(listFriendsMembersData) {
        val user = listLeaderboardMemberRest[it]
        LeaderboardMemberData(
            user.deviceToken ?: "",
            user.firstName ?: "",
            user.lastName ?: "",
            user.place ?: 1,
            user.trips ?: 0,
            user.image ?: "",
            user.isCurrentUser ?: false,
            mappedType,
            user.nickname ?: "",
            user.distance ?: 0.0,
            user.duration ?: 0.0,
            user.value ?: 0.0,
            user.valuePerc ?: 0.0
        )
    }
    return listFriendsMembersData
}

internal fun LeaderboardUserResponse?.toLeadetboardUser(): LeaderboardUser {

    val user = this ?: run {
        return LeaderboardUser()
    }

    return LeaderboardUser(
        user.accelerationPerc ?: 0.0,
        user.accelerationPlace ?: 0,
        user.accelerationScore ?: 0.0,
        user.decelerationPerc ?: 0.0,
        user.decelerationPlace ?: 0,
        user.decelerationScore ?: 0.0,
        user.distance ?: 0.0,
        user.distractionPerc ?: 0.0,
        user.distractionPlace ?: 0,
        user.distractionScore ?: 0.0,
        user.duration ?: 0.0,
        user.distractionPerc ?: 0.0,
        user.place ?: 0,
        user.score ?: 0.0,
        user.speedingPerc ?: 0.0,
        user.speedingPlace ?: 0,
        user.speedingScore ?: 0.0,
        user.trips ?: 0,
        user.turnPerc ?: 0.0,
        user.turnPlace ?: 0,
        user.turnScore ?: 0.0,
        user.usersNumber ?: 0,
        user.tripsPlace ?: 0,
        user.durationPlace ?: 0,
        user.distancePlace ?: 0
    )
}

internal fun LeaderboardUser.toListofLeaderboardUserItems(): List<LeaderboardUserItems> {
    val data = this
    return mutableListOf(
        LeaderboardUserItems(
            type = LeaderboardType.Rate,
            progress = data.usersNumber - data.place.toDouble() + 1,
            place = data.place,
            progressMax = data.usersNumber
        ),
        LeaderboardUserItems(
            LeaderboardType.Acceleration,
            data.usersNumber - data.accelerationPlace.toDouble() + 1,
            data.accelerationPlace,
            data.usersNumber
        ),
        LeaderboardUserItems(
            LeaderboardType.Deceleration,
            data.usersNumber - data.decelerationPlace.toDouble() + 1,
            data.decelerationPlace,
            data.usersNumber
        ),
        LeaderboardUserItems(
            LeaderboardType.Speeding,
            data.usersNumber - data.speedingPlace.toDouble() + 1,
            data.speedingPlace,
            data.usersNumber
        ),
        LeaderboardUserItems(
            LeaderboardType.Distraction,
            data.usersNumber - data.distractionPlace.toDouble() + 1,
            data.distractionPlace,
            data.usersNumber
        ),
        LeaderboardUserItems(
            LeaderboardType.Turn,
            data.usersNumber - data.turnPlace.toDouble() + 1,
            data.turnPlace,
            data.usersNumber
        ),
        LeaderboardUserItems(),
        LeaderboardUserItems(
            LeaderboardType.Trips,
            data.usersNumber - data.tripsPlace.toDouble() + 1,
            data.tripsPlace,
            data.usersNumber
        ),
        LeaderboardUserItems(
            LeaderboardType.Distance,
            data.usersNumber - data.distancePlace.toDouble() + 1,
            data.distancePlace,
            data.usersNumber
        ),
        LeaderboardUserItems(
            LeaderboardType.Duration,
            data.usersNumber - data.durationPlace.toDouble() + 1,
            data.durationPlace,
            data.usersNumber
        )
    )
}

internal fun DailyLimit?.toDailyLimitData(): DailyLimitData {
    this ?: return DailyLimitData()
    return DailyLimitData(this.dailyLimit)
}

internal fun DriveCoinsTotal?.toDriveCoinsTotalData(): DriveCoinsTotalData {
    this ?: return DriveCoinsTotalData()
    return DriveCoinsTotalData(
        this.totalEarnedCoins,
        this.acquiredCoins
    )
}

internal fun DriveCoinsDetailedData.setCompleteData(
    individualData: UserStatisticsIndividualRest?,
    coinsDetailedList: List<DriveCoinsDetailed>?,
    driveCoinsDetailed2: DriveCoinsDetailed2?,
    driveCoinsScoreEco: DriveCoinsScoreEco?
): DriveCoinsDetailedData {

    val detailedData = this

    detailedData.travelingTimeDrivenData = individualData?.drivingTime?.roundToInt()
        ?: 0
    detailedData.travelingMileageData = individualData?.mileageKm?.roundToInt() ?: 0

    var midSpeedingMileage = 0
    var highSpeedingMileage = 0

    coinsDetailedList?.forEach { driveCoinsDetailed ->
        when (driveCoinsDetailed.coinFactor.lowercase()) {
            "EcoScore".lowercase() -> detailedData.ecoDrivingEcoScore =
                driveCoinsDetailed.coinsSum
            "EcoScoreBrakes".lowercase() -> detailedData.ecoDrivingBrakes =
                driveCoinsDetailed.coinsSum
            "EcoScoreDepreciation".lowercase() -> detailedData.ecoDrivingCostOfOwnership =
                driveCoinsDetailed.coinsSum
            "EcoScoreFuel".lowercase() -> detailedData.ecoDrivingFuel =
                driveCoinsDetailed.coinsSum
            "EcoScoreTyres".lowercase() -> detailedData.ecoDrivingTires =
                driveCoinsDetailed.coinsSum

            "Mileage".lowercase() -> detailedData.travelingMileage = driveCoinsDetailed.coinsSum
            "DurationSec".lowercase() -> detailedData.travelingTimeDriven =
                driveCoinsDetailed.coinsSum
            "AccelerationCount".lowercase() -> detailedData.travelingAccelerations =
                driveCoinsDetailed.coinsSum
            "BrakingCount".lowercase() -> detailedData.travelingBrakings =
                driveCoinsDetailed.coinsSum
            "PhoneUsage".lowercase() -> detailedData.travelingPhoneUsage =
                driveCoinsDetailed.coinsSum
            "CorneringCount".lowercase() -> detailedData.travelingCornerings =
                driveCoinsDetailed.coinsSum
            "MidSpeedingMileage".lowercase() -> midSpeedingMileage = driveCoinsDetailed.coinsSum
            "HighSpeedingMileage".lowercase() -> highSpeedingMileage = driveCoinsDetailed.coinsSum

            "SafeScore".lowercase() -> detailedData.safeDrivingCoinsTotal =
                driveCoinsDetailed.coinsSum
        }
    }
    detailedData.travelingSpeeding = midSpeedingMileage + highSpeedingMileage


    detailedData.ecoScore = driveCoinsScoreEco?.ecoScore
        ?.roundToInt() ?: 0
    detailedData.ecoScoreBrakes = driveCoinsScoreEco?.ecoScoreBrakes
        ?.roundToInt() ?: 0
    detailedData.ecoScoreFuel = driveCoinsScoreEco?.ecoScoreFuel
        ?.roundToInt() ?: 0
    detailedData.ecoScoreTyres = driveCoinsScoreEco?.ecoScoreTyres
        ?.roundToInt() ?: 0
    detailedData.ecoScoreCostOfOwnership = driveCoinsScoreEco?.ecoScoreDepreciation
        ?.roundToInt() ?: 0

    detailedData.travellingSum =
        detailedData.travelingMileage + detailedData.travelingTimeDriven + detailedData.travelingAccelerations + detailedData.travelingBrakings + detailedData.travelingCornerings + detailedData.travelingPhoneUsage + detailedData.travelingSpeeding
    detailedData.safeDrivingSum = detailedData.safeDrivingCoinsTotal
    detailedData.ecoDrivingSum =
        detailedData.ecoDrivingEcoScore + detailedData.ecoDrivingBrakes + detailedData.ecoDrivingFuel + detailedData.ecoDrivingTires + detailedData.ecoDrivingCostOfOwnership

    detailedData.travelingAccelerationCount = driveCoinsDetailed2?.accelerationCount
        ?: 0
    detailedData.travelingBrakingCount = driveCoinsDetailed2?.brakingCount ?: 0
    detailedData.travelingCorneringCount = driveCoinsDetailed2?.corneringCount ?: 0
    detailedData.travelingTotalSpeedingKm = driveCoinsDetailed2?.totalSpeedingKm?.roundToInt()
        ?: 0
    detailedData.travelingDrivingTime = driveCoinsDetailed2?.phoneUsage?.roundToInt()
        ?: 0

    return this
}

internal fun StreaksRest?.toStreakList(dateMeasure: DateMeasure): List<Streak> {

    fun getDistance(d: Double) = "${d.roundToInt()} km"
    fun getDate(s: String): String? {
        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH).parse(s) ?: return null

        val format = when (dateMeasure) {
            DateMeasure.MM_DD -> "MM.dd.yy"
            DateMeasure.DD_MM -> "dd.MM.yy"
        }
        val simpleDateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        return simpleDateFormat.format(date)
    }

    val min = "m"

    this ?: return emptyList()
    val data = this

    return listOf(
        Streak(
            StreakCarType.Acceleration,
            getDistance(data.StreakAccelerationCurrentDistanceKm) + " | " + (data.StreakAccelerationCurrentDurationSec.toFloat() / 60f).roundToInt()
                .toString() + " $min",
            getDate(data.StreakAccelerationCurrentFromDate) + " to " + getDate(data.StreakAccelerationCurrentToDate),
            data.StreakAccelerationCurrentStreak.toString(),

            getDistance(data.StreakAccelerationBestDistanceKm) + " | " + (data.StreakAccelerationBestDurationSec.toFloat() / 60f).roundToInt()
                .toString() + " $min",
            getDate(data.StreakAccelerationBestFromDate) + " to " + getDate(data.StreakAccelerationBestToDate),
            data.StreakAccelerationBest.toString()
        ),
        Streak(
            StreakCarType.Braking,
            getDistance(data.StreakBrakingCurrentDistanceKm) + " | " + (data.StreakBrakingCurrentDurationSec.toFloat() / 60f).roundToInt()
                .toString() + " $min",
            getDate(data.StreakBrakingCurrentFromDate) + " to " + getDate(data.StreakBrakingCurrentToDate),
            data.StreakBrakingCurrentStreak.toString(),

            getDistance(data.StreakBrakingBestDistanceKm) + " | " + (data.StreakBrakingBestDurationSec.toFloat() / 60f).roundToInt()
                .toString() + " $min",
            getDate(data.StreakBrakingBestFromDate) + " to " + getDate(data.StreakBrakingBestToDate),
            data.StreakBrakingBest.toString()
        ),
        Streak(
            StreakCarType.Cornering,
            getDistance(data.StreakCorneringCurrentDistanceKm) + " | " + (data.StreakCorneringCurrentDurationSec.toFloat() / 60f).roundToInt()
                .toString() + " $min",
            getDate(data.StreakCorneringCurrentFromDate) + " to " + getDate(data.StreakCorneringCurrentToDate),
            data.StreakCorneringCurrentStreak.toString(),

            getDistance(data.StreakCorneringBestDistanceKm) + " | " + (data.StreakCorneringBestDurationSec.toFloat() / 60f).roundToInt()
                .toString() + " $min",
            getDate(data.StreakCorneringBestFromDate) + " to " + getDate(data.StreakCorneringBestToDate),
            data.StreakCorneringBest.toString()
        ),
        Streak(
            StreakCarType.Speeding,
            getDistance(data.StreakOverSpeedCurrentDistanceKm) + " | " + (data.StreakOverSpeedCurrentDurationSec.toFloat() / 60f).roundToInt()
                .toString() + " $min",
            getDate(data.StreakOverSpeedCurrentFromDate) + " to " + getDate(data.StreakOverSpeedCurrentToDate),
            data.StreakOverSpeedCurrentStreak.toString(),

            getDistance(data.StreakOverSpeedBestDistanceKm) + " | " + (data.StreakOverSpeedBestDurationSec.toFloat() / 60f).roundToInt()
                .toString() + " $min",
            getDate(data.StreakOverSpeedBestFromDate) + " to " + getDate(data.StreakOverSpeedBestToDate),
            data.StreakOverSpeedBest.toString()
        ),
        Streak(
            StreakCarType.PhoneUsage,
            getDistance(data.StreakPhoneUsageCurrentDistanceKm) + " | " + (data.StreakPhoneUsageCurrentDurationSec.toFloat() / 60f).roundToInt()
                .toString() + " $min",
            getDate(data.StreakPhoneUsageCurrentFromDate) + " to " + getDate(data.StreakPhoneUsageCurrentToDate),
            data.StreakPhoneUsageCurrentStreak.toString(),

            getDistance(data.StreakPhoneUsageBestDistanceKm) + " | " + (data.StreakPhoneUsageBestDurationSec.toFloat() / 60f).roundToInt()
                .toString() + " $min",
            getDate(data.StreakPhoneUsageBestFromDate) + " to " + getDate(data.StreakPhoneUsageBestToDate),
            data.StreakPhoneUsageBest.toString()
        )
    )
}

internal fun StreaksRest?.toStreakData(): StreaksData {

    this ?: return StreaksData()
    val data = this

    return StreaksData(
        data.StreakAccelerationBest,
        data.StreakAccelerationBestDurationSec,
        data.StreakAccelerationBestDistanceKm,
        data.StreakAccelerationBestFromDate,
        data.StreakAccelerationBestToDate,
        data.StreakAccelerationCurrentStreak,
        data.StreakAccelerationCurrentDurationSec,
        data.StreakAccelerationCurrentDistanceKm,
        data.StreakAccelerationCurrentFromDate,
        data.StreakAccelerationCurrentToDate,

        data.StreakBrakingBest,
        data.StreakBrakingBestDurationSec,
        data.StreakBrakingBestDistanceKm,
        data.StreakBrakingBestFromDate,
        data.StreakBrakingBestToDate,
        data.StreakBrakingCurrentStreak,
        data.StreakBrakingCurrentDurationSec,
        data.StreakBrakingCurrentDistanceKm,
        data.StreakBrakingCurrentFromDate,
        data.StreakBrakingCurrentToDate,

        data.StreakCorneringBest,
        data.StreakCorneringBestDurationSec,
        data.StreakCorneringBestDistanceKm,
        data.StreakCorneringBestFromDate,
        data.StreakCorneringBestToDate,
        data.StreakCorneringCurrentStreak,
        data.StreakCorneringCurrentDurationSec,
        data.StreakCorneringCurrentDistanceKm,
        data.StreakCorneringCurrentFromDate,
        data.StreakCorneringCurrentToDate,

        data.StreakOverSpeedBest,
        data.StreakOverSpeedBestDurationSec,
        data.StreakOverSpeedBestDistanceKm,
        data.StreakOverSpeedBestFromDate,
        data.StreakOverSpeedBestToDate,
        data.StreakOverSpeedCurrentStreak,
        data.StreakOverSpeedCurrentDurationSec,
        data.StreakOverSpeedCurrentDistanceKm,
        data.StreakOverSpeedCurrentFromDate,
        data.StreakOverSpeedCurrentToDate,

        data.StreakPhoneUsageBest,
        data.StreakPhoneUsageBestDurationSec,
        data.StreakPhoneUsageBestDistanceKm,
        data.StreakPhoneUsageBestFromDate,
        data.StreakPhoneUsageBestToDate,
        data.StreakPhoneUsageCurrentStreak,
        data.StreakPhoneUsageCurrentDurationSec,
        data.StreakPhoneUsageCurrentDistanceKm,
        data.StreakPhoneUsageCurrentFromDate,
        data.StreakPhoneUsageCurrentToDate
    )
}

internal fun UserStatisticsIndividualRest.transformOnDemand(): UserStatisticsIndividualData {
    return UserStatisticsIndividualData(
        this.tripsCount.toInt(),
        this.mileageKm,
        this.mileageMile,
        this.drivingTime.toInt()
    )
}