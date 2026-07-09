package com.example.thesisschedulemanagementapp.data.repository

import com.example.thesisschedulemanagementapp.data.model.Room

class RoomRepository {
    fun defaultRooms(): List<Room> = listOf(
        Room(1, "R407"),
        Room(2, "E515"),
        Room(3, "Auditorium"),
        Room(4, "Cafe Enrique")
    )
}
