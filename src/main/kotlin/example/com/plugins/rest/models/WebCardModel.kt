package example.com.plugins.rest.models

import kotlinx.serialization.Serializable

@Serializable
data class WebCardModel(
    val text: String,
    val title: String,
    val url: String
)
@Serializable
data class WebcardResponse(val cards: List<WebCardModel>)