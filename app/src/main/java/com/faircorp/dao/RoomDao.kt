package com.faircorp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.faircorp.model.Room

@Dao
interface RoomDao {
    // Find all room list order by room's name
    @Query("SELECT * FROM ROOM ORDER BY room_name")
    fun findAll(): List<Room>

    // Find all rooms in a building by building id
    @Query("SELECT * FROM ROOM WHERE building_id = :buildingId ORDER BY room_name")
    fun findAllRoomsByBuilding(buildingId: Long): List<Room>

    @Insert
    suspend fun create(room: Room)

    @Query("SELECT * FROM ROOM WHERE id = :id")
    fun findById(id: Long): Room?

    // Delete room in a building by building id
    @Query("DELETE FROM ROOM WHERE building_id = :buildingId")
    suspend fun clearByBuildingId(buildingId: Long)

    // Delete room by id
    @Query("DELETE FROM ROOM WHERE id = :id")
    suspend fun deleteById(id: Long)


}