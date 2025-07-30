package example.com.plugins.data.repositories

import example.com.plugins.utility.AppLogger
import retrofit2.HttpException

suspend fun <T> handleRequestError(request: suspend () -> T): Result<T> {
    return try {
        val result = request.invoke()
        AppLogger.log("Result: $result")
        Result.Success(result)
    } catch (e: Exception) {
        if(e is HttpException){
            val reset = "\u001B[0m"
            val red = "\u001B[31m"
            val errorBody = e.response()?.errorBody()
            val errorMessage = errorBody?.string() // Получаем тело ошибки как строку
            AppLogger.log("\n${red}Error: $errorMessage$reset\n")
        }else{
            AppLogger.log(e)
        }
        Result.Error(e)
    }
}