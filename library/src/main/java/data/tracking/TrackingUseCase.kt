package data.tracking

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import com.raxeltelematics.v2.sdk.services.main.elm.BluetoothUtils
import com.raxeltelematics.v2.sdk.services.main.elm.Constants
import data.utils.ImageLoader
import domain.model.tracking.TripData
import domain.model.tracking.TripDetailsData
import domain.repository.SettingsRepo
import domain.repository.TrackingApiRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

internal  class TrackingUseCase constructor(
    context: Context,
    private val trackingApiRepo: TrackingApiRepo,
    private val imageLoader: ImageLoader
) {

    private var notificationIntent: Intent? = null

    init {
        trackingApiRepo.setContext(context)
    }

    fun setDeviceToken(deviceToken: String) {

        if (deviceToken.isNotBlank())
            trackingApiRepo.setDeviceToken(deviceToken)
    }

    fun checkPermissions(): Flow<Boolean> {
        return trackingApiRepo.checkPermissions()
    }

    fun startWizard(activity: Activity) {
        trackingApiRepo.checkPermissionAndStartWizard(activity)
    }

    fun disableTrackingSDK() {
        trackingApiRepo.setEnableTrackingSDK(false)
    }

    fun startTracking() {
        trackingApiRepo.startTracking()
    }

    fun stopTracking() {
        trackingApiRepo.stopTracking()
    }

    fun setIntentForNotification(intent: Intent) {

        notificationIntent = intent
        trackingApiRepo.setIntentForNotification(intent)
    }

    fun enableTracking() {

        trackingApiRepo.setEnableTrackingSDK(true)
        notificationIntent?.let { notificationIntent ->
            trackingApiRepo.setIntentForNotification(notificationIntent)
        }
    }

    fun logout() {
        trackingApiRepo.logout()
    }


    fun getLastTrip(): Flow<TripData?> {

        return flow {
            val data = trackingApiRepo.getLastTrack()
            emit(data)
        }
    }

    fun getTripImage(token: String): Flow<Bitmap?> {

        return flow {
            val data = trackingApiRepo.getTrackImageHolder(token) ?: return@flow emit(null)
            val bitmap = imageLoader.loadImage(data.url, data.r, token)
            emit(bitmap)
        }
    }

    fun getTrips(offset: Int, limit: Int): Flow<List<TripData>> {

        return flow {
            val data = trackingApiRepo.getTrips(offset, limit)
            emit(data)
        }
    }

    fun getTripDetailsByPos(position: Int): Flow<TripDetailsData?> {
        return flow {
            val tripData = trackingApiRepo.getTrips(position, 1).firstOrNull()
            tripData?.id?.let { tripId ->
                val data = trackingApiRepo.getTripDetails(tripId)
                emit(data)
            } ?: run {
                emit(null)
            }
        }
    }

    fun hideTrip(tripId: String): Flow<Unit> {

        return flow {
            val data = trackingApiRepo.hideTrip(tripId)
            emit(data)
        }
    }

    fun getLastSession(): Flow<Long> {

        return flow {
            val data = trackingApiRepo.getLastSession()
            emit(data)
        }
    }

    fun getBluetoothAdapter(context: Context): BluetoothAdapter? {

        return BluetoothUtils.getBluetoothAdapter(context)
    }

    fun getRequestBluetoothEnableCode() = Constants.REQUEST_BLUETOOTH_ENABLE_CODE

    fun getElmDevice(): Flow<Unit> {

        return flow {
            emit(trackingApiRepo.getElmDevice())
        }
    }

    fun removeFutureTrackTag(tag: String): Flow<Unit> {

        return flow {
            val data = trackingApiRepo.removeFutureTrackTag(tag)
            emit(data)
        }
    }

    fun addFutureTrackTag(tag: String): Flow<Unit> {

        return flow {
            val data = trackingApiRepo.addFutureTrackTag(tag)
            emit(data)
        }
    }
}