package example.com.plugins.repositories

suspend fun <T> handleRequestError(request: suspend () -> T): Result<T> {
    return try {
        Result.Success(request.invoke())
    } catch (e: Exception) {
        println(e)
        Result.Error(e)
    }
}