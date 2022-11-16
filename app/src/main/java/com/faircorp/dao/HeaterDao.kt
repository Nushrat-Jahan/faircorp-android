package com.faircorp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.faircorp.model.Heater

@Dao
interface HeaterDao {
    // Find all heater order by heater's name
    @Query("SELECT * FROM HEATER ORDER BY name")
    fun findAll(): List<Heater>

    // Find all heaters a room contains by room id
    @Query("SELECT * FROM HEATER WHERE room_id = :roomId order by name")
    fun findHeatersByRoomId(roomId: Long): List<Heater>

    // Find heater by heater id
    @Query("SELECT * FROM HEATER WHERE id = :id")
    fun findById(id: Long): Heater?

    @Insert
    suspend fun create(heater: Heater)

    // Delete heaters in a room by room id
    @Query("DELETE FROM HEATER WHERE room_id = :roomId")
    suspend fun clearByRoomId(roomId: Long)

    @Query("SELECT * FROM HEATER WHERE heater_status = :status")
    fun findAllHeatersByStatus(status: String): List<Heater>

    // Delete heater by id
    @Query("DELETE FROM HEATER WHERE id = :id")
    suspend fun deleteById(id: Long)
}