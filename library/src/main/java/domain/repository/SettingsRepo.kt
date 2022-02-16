package domain.repository

import android.content.Context
import domain.model.measures.DateMeasure
import domain.model.measures.DistanceMeasure
import domain.model.measures.TimeMeasure

internal interface SettingsRepo {

    fun getTelematicsLink(context: Context): String

    fun getDateMeasure(): DateMeasure
    fun getDistanceMeasure(): DistanceMeasure
    fun getTimeMeasure(): TimeMeasure

    fun setDateMeasure(dateMeasure: DateMeasure)
    fun setDistanceMeasure(distanceMeasure: DistanceMeasure)
    fun setTimeMeasure(timeMeasure: TimeMeasure)
}