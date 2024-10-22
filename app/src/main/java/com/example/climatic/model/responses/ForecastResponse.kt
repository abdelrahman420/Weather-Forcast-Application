package com.example.climatic.model.responses

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.climatic.model.dtos.City
import com.example.climatic.model.dtos.Wlist
import com.google.gson.annotations.SerializedName

@Entity(tableName = "locations_table")
data class ForecastResponse(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @SerializedName("cod") var cod: String? = null,
    @SerializedName("message") var message: Int? = null,
    @SerializedName("cnt") var cnt: Int? = null,
    @SerializedName("list") var list: ArrayList<Wlist> = arrayListOf(),
    @SerializedName("city") var city: City? = City()
)

