package com.example.climatic.model.dtos

import com.google.gson.annotations.SerializedName

data class Clouds(

    @SerializedName("all") var all: Int? = null

)