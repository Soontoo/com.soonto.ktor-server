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


private class CombineLogger: Logger{
    val fileLogger = FileLogger()
    val consoleLogger = CombineLogger()
    override fun log(message: String) {
        fileLogger.log(message)
        consoleLogger.log(message)
    }
    override fun log(message: Exception) {
        fileLogger.log(message)
        consoleLogger.log(message)
    }
}

val AppLogger: Logger = ConsoleLogger()


fun printlnRED(message: String) = println("\u001B[31m$message\u001B[0m")