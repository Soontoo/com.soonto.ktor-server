package example.com.plugins.data.yandexGptApi

import example.com.AppConfig
import kotlinx.serialization.Serializable

@Serializable
data class GetEmbeddingRequest(
    val text: String,
    val modelUri: String = "emb://${AppConfig.yandexFolderId}/text-search-query/latest",
)