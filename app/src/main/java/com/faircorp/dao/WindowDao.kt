package com.faircorp.dao

import androidx.room.*
import com.faircorp.model.Window

@Dao
interface WindowDao {
    // Select query to get list of windows ordered by window name
    @Query("SELECT * FROM RWINDOW ORDER BY name")
    fun findAll(): List<Window>

    // Find window by id
    @Query("SELECT * FROM RWINDOW WHERE id = :id")
    fun findById(id: Long): Window?

    @Query("SELECT * FROM RWINDOW WHERE room_id = :roomId order by name")
    fun findByRoomId(roomId: Long): List<Window>

    @Insert
    suspend fun create(window: Window)

    // Delete windows of room by room id
    @Query("DELETE FROM RWINDOW WHERE room_id = :roomId")
    suspend fun clearByRoomId(roomId: Long)

    // Delete window by id
    @Query("DELETE FROM RWINDOW WHERE id = :id")
    suspend fun deleteById(id: Long)
}