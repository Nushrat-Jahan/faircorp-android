package com.faircorp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faircorp.dto.HeaterDto
import com.faircorp.dto.HeaterStatus

//HEATER table contains heater id, name, power, heater status(ON or OFF) and room id
@Entity(tableName = "HEATER")
data class Heater(
    @PrimaryKey val id: Long?,
    @ColumnInfo val name: String,
    @ColumnInfo(name = "room_id") val roomId: Long,
    @ColumnInfo(name = "power") val power: Long?,
    @ColumnInfo(name = "heater_status") val heaterStatus: HeaterStatus
) {
    fun toDto(): HeaterDto =
        HeaterDto(
            id,
            name,
            power,
            roomId,
            heaterStatus
        )

}