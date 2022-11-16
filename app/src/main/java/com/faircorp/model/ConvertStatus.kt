package com.faircorp.model

import androidx.room.TypeConverter
import com.faircorp.dto.HeaterStatus
import com.faircorp.dto.WindowStatus

class ConvertStatus {

    @TypeConverter
    fun fromWindowStatus(value: WindowStatus?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toWindowStatus(value: String?): WindowStatus? {
        return value?.let { WindowStatus.valueOf(it) }
    }

    @TypeConverter
    fun fromHeaterStatus(value: HeaterStatus?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun toHeaterStatus(value: String?): HeaterStatus? {
        return value?.let { HeaterStatus.valueOf(it) }
    }

}