package com.example.climatic.model.responses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.climatic.model.dtos.City
import com.example.climatic.model.dtos.Wlist
import com.google.gson.annotations.SerializedName


@Entity(tableName = "forecast_table")
data class ForecastResponse(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @SerializedName("cod") var cod: String? = null,
    @SerializedName("message") var message: Int? = null,
    @SerializedName("cnt") var cnt: Int? = null,
    @SerializedName("list") var list: ArrayList<Wlist> = arrayListOf(),
    @SerializedName("city") var city: City? = City()
)
fun ForecastResponse.toHourlyResponse(): List<Wlist> {
    return list.take(8)
}


fun ForecastResponse.getFiveDaysForecast(): List<Wlist> {
    val indices = (0 until 5).map { it * 8 }
    return indices.filter { it < list.size }.map { list[it] }
}




