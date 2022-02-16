package data.api.errors

import java.io.IOException

internal data class ApiError (
    val errorCode: Int,
    val msg: String? = null,
) : IOException(msg)