package example.com

object AppConfig {
    fun initialize() {
        databaseUrl = System.getenv("DB_URL")
        databaseUser = System.getenv("DB_NAME")
        databasePassword = System.getenv("DB_PASSWORD")
        yandexIAMToken = System.getenv("IAM_TOKEN")
        yandexFolderId = System.getenv("FOLDER_ID")
        serverUrl = System.getenv("SERVER_URL")
//        keyStore = System.getenv("KEYSTORE")
        keyAlias = System.getenv("KEY_ALIAS")
        keyStorePassword = System.getenv("KEYSTORE_PASSWORD")
        privateKeyPassword = System.getenv("KEY_PASSWORD")
        privateKeyPassword = System.getenv("KEY_PASSWORD")
        port =  System.getenv("PORT")
        sslPort =  System.getenv("SSL_PORT")
    }

    lateinit var databaseUrl: String
    lateinit var databaseUser: String
    lateinit var databasePassword: String
    lateinit var yandexIAMToken: String
    lateinit var yandexFolderId: String
    lateinit var serverUrl: String
//    lateinit var keyStore: String
    lateinit var keyAlias: String
    lateinit var keyStorePassword: String
    lateinit var privateKeyPassword: String
    lateinit var port: String
    lateinit var sslPort: String
}