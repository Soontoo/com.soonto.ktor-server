package example.com.plugins.utility

import java.io.File

interface Logger {
    fun log(message: String)
    fun log(message: Exception)
}


private class ConsoleLogger : Logger {
    override fun log(message: String) {
        println(message)
    }
    override fun log(message: Exception) {
        println(message)
    }
}

private class FileLogger : Logger {
    override fun log(message: String) {
        File("logs.txt").appendText("$message\n")
    }
    override fun log(message: Exception) {
        File("logs.txt").appendText("$message\n")
    }
}

val AppLogger: Logger = ConsoleLogger()