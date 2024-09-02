package example.com.plugins.data.repositories

import retrofit2.HttpException

suspend fun <T> handleRequestError(request: suspend () -> T): Result<T> {
    return try {
        Result.Success(request.invoke())
    } catch (e: Exception) {
        if(e is HttpException){
            val reset = "\u001B[0m"
            val red = "\u001B[31m"
            val errorBody = e.response()?.errorBody()
            val errorMessage = errorBody?.string() // Получаем тело ошибки как строку
            println("\n${red}Error: $errorMessage$reset\n")
        }
        Result.Error(e)
    }
}