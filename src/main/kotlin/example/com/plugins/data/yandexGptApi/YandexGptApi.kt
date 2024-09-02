package example.com.plugins.data.yandexGptApi

import example.com.plugins.network.RetrofitProvider
import example.com.plugins.network.YandexGptService

val yandexGptApi: YandexGptService =  RetrofitProvider.get.create(YandexGptService::class.java)