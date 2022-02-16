package data.model.rest

import com.google.gson.annotations.SerializedName

internal open class BaseResponse  {

    @SerializedName("Code") var code: Int = 0
    @SerializedName("Message") var message: String? = null
}