package data.interceptor

import okhttp3.Interceptor
import okhttp3.Response

internal  class InstanceValuesInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .build()
        return chain.proceed(request)
    }
}