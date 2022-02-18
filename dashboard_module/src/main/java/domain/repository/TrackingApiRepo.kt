package domain.repository

import android.app.Activity
import android.content.Context
import android.content.Intent
import domain.model.tracking.TripData
import domain.model.tracking.TripDetailsData
import domain.model.tracking.TripImageHolder
import kotlinx.coroutines.flow.Flow

internal interface TrackingApiRepo {

    /** handle sdk*/
    fun setContext(context: Context)
    fun setDeviceToken(deviceId: String)

    fun checkPermissions(): Flow<Boolean>
    fun checkPermissionAndStartWizard(activity: Activity)

    fun enableTrackingSDK()
    fun setEnableTrackingSDK(enable: Boolean)

    fun startTracking()
    fun stopTracking()

    fun setIntentForNotification(intent: Intent)

    fun logout()

    /** handle tracks */
    suspend fun getLastTrack(): TripData?
    suspend fun getTrackImageHolder(trackId: String): TripImageHolder?
    suspend fun getTrips(offset: Int, limit: Int): List<TripData>

    suspend fun getTripDetails(tripId: String): TripDetailsData?

    suspend fun hideTrip(tripId: String)

    /** handle elm */
    suspend fun getLastSession(): Long
    suspend fun getElmDevice()

    /** tags */
    suspend fun removeFutureTrackTag(tag: String)
    suspend fun addFutureTrackTag(tag: String)
}