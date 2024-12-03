package com.sametdundar.guaranteeapp.roomdatabase

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromImageUrisList(value: List<String>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toImageUrisList(value: String?): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
}
