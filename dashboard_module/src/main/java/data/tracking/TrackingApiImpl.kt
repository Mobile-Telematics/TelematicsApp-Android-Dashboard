package data.tracking

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.raxeltelematics.v2.sdk.Settings
import com.raxeltelematics.v2.sdk.TrackingApi
import com.raxeltelematics.v2.sdk.server.model.sdk.TrackTag
import com.raxeltelematics.v2.sdk.utils.permissions.PermissionsWizardActivity
import data.model.tracking.TripsMapper
import domain.model.tracking.TripData
import domain.model.tracking.TripDetailsData
import domain.model.tracking.TripImageHolder
import domain.repository.TrackingApiRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@SuppressLint("MissingPermission")
internal class TrackingApiImpl  constructor(
    private val tripsMapper: TripsMapper
) : TrackingApiRepo {

    private val TAG = "TrackingApiImpl"

    private val trackingApi = TrackingApi.getInstance()

    private var tripData: TripData? = null

    private val locale = com.raxeltelematics.v2.sdk.server.model.Locale.EN

    override fun setContext(context: Context) {
        val setting = Settings(
            Settings.stopTrackingTimeHigh, 150,
            autoStartOn = true,
            hfOn = true,
            elmOn = true
        )
        TrackingApi.getInstance().initialize(context, setting)
    }

    override fun setDeviceToken(deviceId: String) {
        Log.d(TAG, "setDeviceToken: deviceId $deviceId")
        trackingApi.setDeviceID(deviceId)
    }

    override fun checkPermissionAndStartWizard(activity: Activity) {

        if (!trackingApi.isAllRequiredPermissionsAndSensorsGranted()) {
            activity.startActivityForResult(
                PermissionsWizardActivity.getStartWizardIntent(
                    activity,
                    enableAggressivePermissionsWizard = true,
                    enableAggressivePermissionsWizardPage = true
                ), PermissionsWizardActivity.WIZARD_PERMISSIONS_CODE
            )
        }
    }

    override fun checkPermissions(): Flow<Boolean> {

        return flow {
            val data = trackingApi.isAllRequiredPermissionsGranted()
            emit(data)
        }
    }

    override fun enableTrackingSDK() {

        trackingApi.setEnableSdk(true)
    }

    override fun setEnableTrackingSDK(enable: Boolean) {

        trackingApi.setEnableSdk(enable)
    }

    override fun setIntentForNotification(intent: Intent) {

        TrackingApi.getInstance().setIntentForNotification(intent)
    }

    override fun logout() {
        trackingApi.setEnableSdk(false)
        trackingApi.clearDeviceID()
        trackingApi.setEnableSdk(false)
    }

    override suspend fun getLastTrack(): TripData? {

        tripData = null
        val arrayOfTracks = trackingApi.getTracks(
            locale,
            null,
            null,
            0,
            20
        )
        val listTripData =
            tripsMapper.transformTripsList(arrayOfTracks.asList()).filter { !it.isDeleted }
        tripsMapper.sort(listTripData, tripData)
        if (listTripData.isNotEmpty()) {
            tripData = listTripData[0]
        }
        return listTripData.firstOrNull()
    }

    override suspend fun getTrackImageHolder(trackId: String): TripImageHolder? {
        val trackDetails =
            trackingApi.getTrackDetails(trackId, locale)
        return tripsMapper.transformTripDetails(trackDetails)?.let {
            tripsMapper.transform(it)
        }
    }

    override suspend fun getTrips(offset: Int, limit: Int): List<TripData> {

        if (offset == 0) tripData = null
        val arrayOfTracks = trackingApi.getTracks(
            locale,
            null,
            null,
            offset,
            limit
        )
        val listTripData =
            tripsMapper.transformTripsList(arrayOfTracks.asList()).filter { !it.isDeleted }
        tripsMapper.sort(listTripData, tripData)
        if (listTripData.isNotEmpty()) {
            tripData = listTripData[0]
        }
        return listTripData
    }

    override suspend fun getTripDetails(tripId: String): TripDetailsData? {

        val trackDetails = trackingApi.getTrackDetails(tripId, locale)
        return tripsMapper.transformTripDetails(trackDetails)
    }

    override suspend fun hideTrip(tripId: String) {

        val tag = TrackTag(tag = "DEL")
        trackingApi.addTrackTags(tripId, arrayOf(tag))
    }

    override suspend fun getLastSession(): Long {

        val data = trackingApi.getElmManager()?.getLastSession()
        return data?.second ?: 0
    }


    override suspend fun getElmDevice() {

        trackingApi.getElmManager()?.getElmDevices()
    }

    override fun startTracking() {
        trackingApi.startTracking()
    }

    override fun stopTracking() {
        trackingApi.stopTracking()
    }

    override suspend fun removeFutureTrackTag(tag: String) {
        trackingApi.removeFutureTrackTag(tag)
    }

    override suspend fun addFutureTrackTag(tag: String) {
        trackingApi.addFutureTrackTag(tag)
    }
}