package com.faircorp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faircorp.dto.RoomDto
import com.faircorp.dto.WindowDto
import com.faircorp.dto.WindowStatus

// WINDOW table contains window id, name, window status(OPEN or CLOSED), room id and room name
@Entity(tableName = "RWINDOW")
data class Window(
    @PrimaryKey val id: Long?,
    @ColumnInfo val name: String,
    @ColumnInfo(name = "room_id") val roomId: Long,
    @ColumnInfo(name = "room_name") val roomName: String,
    @ColumnInfo(name = "window_status") val windowStatus: WindowStatus
) {
    fun toDto(): WindowDto = WindowDto(id, name, roomId, roomName, windowStatus)
}