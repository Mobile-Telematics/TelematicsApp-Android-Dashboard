package data.utils

internal sealed class Resource<T> {

    class Success<T>(val data: T? = null) : Resource<T>()

    class Loading<T> : Resource<T>()

    class Failure<T>(val error: Throwable? = null) : Resource<T>()
}