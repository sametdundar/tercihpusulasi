package com.tercihpusulasi.tercihpusulasi.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object JsonConverter {
    val gson = Gson()

    // Nesneyi JSON formatına dönüştür
    fun <T> toJson(data: T): String {
        return gson.toJson(data)
    }

    // JSON'dan nesne oluştur
    inline fun <reified T> fromJson(json: String): T {
        return gson.fromJson(json, object : TypeToken<T>() {}.type)
    }

    // JSON'dan liste oluştur
    inline fun <reified T> fromJsonList(json: String): List<T> {
        return gson.fromJson(json, object : TypeToken<List<T>>() {}.type)
    }
}

