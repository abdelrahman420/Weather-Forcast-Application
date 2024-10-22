package com.example.climatic.model.database

import androidx.room.TypeConverter
import com.example.climatic.model.dtos.City
import com.example.climatic.model.dtos.Wlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class Converters {

    @TypeConverter
    fun fromArrayList(value: ArrayList<Wlist>?): String? {
        // Convert ArrayList to JSON String using Gson
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<Wlist>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toArrayList(value: String?): ArrayList<Wlist>? {
        // Convert JSON String back to ArrayList using Gson
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<Wlist>>() {}.type
        return gson.fromJson(value, type)
    }
    @TypeConverter
    fun fromCity(city: City?): String? {
        // Convert City object to JSON String using Gson
        val gson = Gson()
        return gson.toJson(city)
    }

    @TypeConverter
    fun toCity(cityJson: String?): City? {
        // Convert JSON String back to City object using Gson
        val gson = Gson()
        return gson.fromJson(cityJson, City::class.java)
    }
}
