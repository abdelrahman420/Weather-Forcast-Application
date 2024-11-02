package com.example.climatic.model.dtos

import androidx.annotation.NonNull
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favourites", primaryKeys = ["lat", "lon"])

data class Favourites(
    @NonNull
    @SerializedName("lat") var lat: Double,
    @NonNull
    @SerializedName("lon") var lon: Double,


    )