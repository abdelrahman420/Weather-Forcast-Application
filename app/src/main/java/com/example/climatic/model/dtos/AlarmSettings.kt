package com.example.climatic.model.dtos

data class AlarmSettings(
    val id: Int,
    val duration: Long,
    val alarmType: String,
    val hour: Int,
    val minute: Int
)