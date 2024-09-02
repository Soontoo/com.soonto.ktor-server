package example.com.plugins.data.repositories

sealed class Result<T>{
    class Success<T>(val data: T) : Result<T>()
    class Error<T>(val error: Throwable) : Result<T>()
}

fun <T, R> Result<R>.mapModel(mapper: (R) -> T): Result<T> {
    return when (this) {
        is Result.Success -> Result.Success(mapper(data))
        is Result.Error -> Result.Error(error)
    }
}