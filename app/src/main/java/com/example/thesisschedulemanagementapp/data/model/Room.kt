package com.example.thesisschedulemanagementapp.data.model

import com.google.gson.annotations.SerializedName

data class Room(
    @SerializedName("room_id") val roomId: Int,
    @SerializedName("room_name") val roomName: String
)
