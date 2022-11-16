package com.faircorp.faircorpDb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.faircorp.dao.BuildingDao
import com.faircorp.dao.HeaterDao
import com.faircorp.dao.RoomDao
import com.faircorp.dao.WindowDao
import com.faircorp.model.*

// Used RoomDatabase for faircorp
@Database(entities = [Window::class, Room::class,Building::class,Heater::class], version = 1)
@TypeConverters(ConvertStatus::class)
abstract class FaircorpDb : RoomDatabase() {
    abstract fun windowDao(): WindowDao
    abstract fun roomDao(): RoomDao
    abstract fun buildingDao(): BuildingDao
    abstract fun heaterDao(): HeaterDao
}