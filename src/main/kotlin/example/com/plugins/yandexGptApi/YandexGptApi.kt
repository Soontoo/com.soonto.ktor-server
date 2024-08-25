package example.com.plugins.yandexGptApi

import example.com.plugins.network.RetrofitProvider
import example.com.plugins.network.YandexGptService

val yandexGptApi: YandexGptService =  RetrofitProvider.get.create(YandexGptService::class.java)