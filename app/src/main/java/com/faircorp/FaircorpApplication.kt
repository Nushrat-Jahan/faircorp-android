package com.faircorp

import android.app.Application
import androidx.room.Room
import com.faircorp.faircorpDb.FaircorpDb

class FaircorpApplication : Application() {
    var count = 0
    val database: FaircorpDb by lazy {
        if (count == 0) {
            applicationContext.deleteDatabase("faircorpdb")
            count++
        }
        Room.databaseBuilder(this, FaircorpDb::class.java, "faircorpdb")
            .fallbackToDestructiveMigration()
            .build()
    }
}