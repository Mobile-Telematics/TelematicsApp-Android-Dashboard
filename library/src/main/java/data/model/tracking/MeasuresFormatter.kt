package data.model.tracking

import domain.model.measures.DistanceMeasure
import java.util.*


internal interface MeasuresFormatter {

    fun getFullNewDate(date: Date?): String
    fun getDateMonthDay(date: Date?): String
    fun parseDate(date: String): Date?
    fun getTime(date: Date?): String
    fun getDateYearTime(date: Date?): String

    fun parseFullNewDate(date: String): Date?
    fun getDateWithTime(date: Date?): String

    fun getDistanceByKm(km: Double): Double
    fun getDistanceByKm(km: Float): Double
    fun getDistanceByKm(km: Int): Double
    fun getDistanceMeasureValue(): DistanceMeasure

    fun getDateForDemandMode(time:Long?): String
}