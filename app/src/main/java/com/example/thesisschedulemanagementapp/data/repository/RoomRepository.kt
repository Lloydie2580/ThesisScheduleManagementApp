package com.example.thesisschedulemanagementapp.data.repository

import com.example.thesisschedulemanagementapp.data.model.Room

class RoomRepository {
    fun defaultRooms(): List<Room> = listOf(
        Room(1, "Room TBA"),
        Room(2, "R407"),
        Room(3, "E515"),
        Room(4, "Auditorium"),
        Room(5, "Cafe Enrique")
    )
}
