package com.example.climatic.model.responses

import androidx.room.Entity
import com.example.climatic.model.dtos.Clouds
import com.example.climatic.model.dtos.Coord
import com.example.climatic.model.dtos.Main
import com.example.climatic.model.dtos.Sys
import com.example.climatic.model.dtos.Weather
import com.example.climatic.model.dtos.Wind
import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather_table")
data class WeatherResponse(

    @SerializedName("coord") var coord: Coord? = Coord(),
    @SerializedName("weather") var weather: ArrayList<Weather> = arrayListOf(),
    @SerializedName("base") var base: String? = null,
    @SerializedName("main") var main: Main? = Main(),
    @SerializedName("visibility") var visibility: Int? = null,
    @SerializedName("wind") var wind: Wind? = Wind(),
    @SerializedName("clouds") var clouds: Clouds? = Clouds(),
    @SerializedName("dt") var dt: Long? = null,
    @SerializedName("sys") var sys: Sys? = Sys(),
    @SerializedName("timezone") var timezone: Int? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("cod") var cod: Int? = null

)

