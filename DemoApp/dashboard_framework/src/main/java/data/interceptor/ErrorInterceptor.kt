package data.interceptor

import android.util.Log
import com.google.gson.Gson
import data.api.errors.ApiError
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import org.json.JSONObject

internal  class ErrorInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val bodyString = response.body!!.string()

        val code = response.code

        Log.d(
            "ErrorInterceptor",
            "code: $code url: ${request.url.toUri()} msg: ${response.message}"
        )

        try {
            if (!bodyString.isNullOrEmpty()) {
                val p = JSONObject(bodyString)
                if (p.has("Status")) {
                    val status = p.getInt("Status")
                    Log.d("ErrorInterceptor", "apiResponse.status: $status")
                    if (status != 200) {
                        throw ApiError(status)
                    }
                }
            }
        } catch (e: Exception) {
            throw ApiError(code)
        }


        return response.newBuilder()
            .body(ResponseBody.create(response.body?.contentType(), bodyString))
            .build()
    }
}