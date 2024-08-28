package example.com.plugins.repositories

import example.com.plugins.network.YandexGptService
import example.com.plugins.yandexGptApi.RequestMessage
import example.com.plugins.yandexGptApi.RequestModel


suspend fun YandexGptService.sendQuestion(question: String): Result<String> {
    return handleRequestError {
        sendMessage(RequestModel(messages = listOf(RequestMessage(text = question)))).result.alternatives.joinToString {
            it.message.text
        }
    }
}

//class YandexGptRepository(private val yandexGptApi: YandexGptService) {
//    suspend fun sentQuestion(question: String): Result<String> {
//        return handleRequestError {
//            yandexGptApi.sendMessage(
//                RequestModel(messages = listOf(RequestMessage(text = question)))
//            ).result.alternatives.joinToString {
//                it.message.text
//            }
//        }
//    }
//}