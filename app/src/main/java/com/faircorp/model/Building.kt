package com.faircorp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.faircorp.dto.BuildingDto

// BUILDING table contains building id, name and address
@Entity(tableName = "BUILDING")
data class Building(
    @PrimaryKey val id: Long?,
    @ColumnInfo(name = "building_name") val name: String,
    @ColumnInfo(name = "building_address") val address: String
){
    fun toDto(): BuildingDto =
        BuildingDto(id, name, address)
}
