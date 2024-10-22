package com.example.climatic.model.dtos

import com.google.gson.annotations.SerializedName

data class Sys(

    @SerializedName("pod") var pod: String? = null

)