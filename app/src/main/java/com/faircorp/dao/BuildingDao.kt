package com.faircorp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.faircorp.model.Building

@Dao
interface BuildingDao {
    // Find all buildings order by building id
    @Query("SELECT * FROM BUILDING ORDER BY building_name")
    fun findAll(): List<Building>

    // Find building by building id
    @Query("SELECT * FROM BUILDING WHERE id = :id")
    fun findById(id: Long): Building?

    @Insert
    suspend fun create(BUILDING: Building)

    @Query("DELETE FROM BUILDING")
    suspend fun clearAll()

    // Delete building by building id
    @Query("DELETE FROM BUILDING WHERE id = :id")
    suspend fun deleteById(id: Long)

}