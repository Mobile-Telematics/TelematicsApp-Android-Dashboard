package data.model.rest

import com.google.gson.annotations.SerializedName

internal data class ErrorData(
    @SerializedName("Key")
    val key: String?,
    @SerializedName("Message")
    val message: String?
)