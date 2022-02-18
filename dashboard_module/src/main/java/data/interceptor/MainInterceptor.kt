package data.interceptor

import data.Globals
import kotlinx.coroutines.runBlocking
import okhttp3.*

internal  class MainInterceptor : Interceptor, Authenticator {
    private val monitor = Any()
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        runBlocking {
            val accessToken = Globals.ACCESS_TOKEN
            request = request.newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()
        }
        return chain.proceed(request)
    }

    override fun authenticate(route: Route?, response: Response): Request {
        synchronized(monitor) {
            return runBlocking {
                val request = response.request
                (response.request.header("Authorization") != Globals.ACCESS_TOKEN)
                request.newBuilder().removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer ${Globals.ACCESS_TOKEN}").build()

            }
        }
    }
}