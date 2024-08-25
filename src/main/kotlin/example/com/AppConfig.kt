package example.com

import io.ktor.server.application.*

object AppConfig{
    fun initialize(environment: ApplicationEnvironment){
        databaseUrl = environment.config.property("ktor.environment.db_url").getString()
        databaseUser = environment.config.property("ktor.environment.db_name").getString()
        databasePassword = environment.config.property("ktor.environment.db_password").getString()
        yandexIAMToken = environment.config.property("ktor.environment.yandex_iam_token").getString()
        yandexFolderId = environment.config.property("ktor.environment.yandex_folder_id").getString()
        serverUrl = environment.config.property("ktor.environment.server_url").getString()
    }
    lateinit var databaseUrl: String
    lateinit var databaseUser: String
    lateinit var databasePassword: String
    lateinit var yandexIAMToken: String
    lateinit var yandexFolderId: String
    lateinit var serverUrl: String
}