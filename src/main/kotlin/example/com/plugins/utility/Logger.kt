package example.com.plugins.utility

import java.io.File

interface Logger {
    fun log(message: String)
    fun log(message: Exception)
    fun log(message: Throwable)
}

const val GRAFANA_TAG = "grafana_logs"
const val GRAFANA_INFO_TAG = "${GRAFANA_TAG}_info: "
const val GRAFANA_ERROR_TAG = "${GRAFANA_TAG}_error: "
private class ConsoleLogger : Logger {
    override fun log(message: String) {
        println(GRAFANA_INFO_TAG + message)
    }
    override fun log(message: Exception) {
        println(GRAFANA_ERROR_TAG + message)
    }

    override fun log(message: Throwable) {
        println(GRAFANA_ERROR_TAG + message)
    }
}

private class FileLogger : Logger {
    override fun log(message: String) {
        File("logs.txt").appendText("$message\n")
    }
    override fun log(message: Exception) {
        File("logs.txt").appendText("$message\n")
    }

    override fun log(message: Throwable) {
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

    override fun log(message: Throwable) {
        fileLogger.log(message)
        consoleLogger.log(message)
    }
}

val AppLogger: Logger = ConsoleLogger()


fun printlnRED(message: String) = println("\u001B[31m$message\u001B[0m")