package com.faircorp.controller

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.liveData
import androidx.lifecycle.viewmodel.CreationExtras
import com.faircorp.FaircorpApplication
import com.faircorp.dao.HeaterDao
import com.faircorp.dto.HeaterDto
import com.faircorp.model.Heater
import com.faircorp.api.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HeaterController(private val heaterDao: HeaterDao) : MainController() {
    companion object {
        val factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val heaterDao = (extras[APPLICATION_KEY] as FaircorpApplication).database.heaterDao()
                return HeaterController(heaterDao) as T
            }
        }
    }

    fun findHeatersByRoomId(roomId: Long) : LiveData<List<HeaterDto>> = liveData {
        val elements: List<HeaterDto> = withContext(Dispatchers.IO) {
            try {
                val response = ApiServices.heaterApi.findHeatersByRoomId(roomId).execute()
                withContext(Dispatchers.Main) {
                    networkState.value = State.ONLINE
                }
                val heaters: List<HeaterDto> = response.body() ?: emptyList()
                heaters.apply {
                    heaterDao.clearByRoomId(roomId)
                    Log.e("heaters", heaters.toString())
                    forEach {
                        heaterDao.create(
                            Heater(
                                id = it.id,
                                name = it.name,
                                power = it.power,
                                heaterStatus = it.heaterStatus,
                                roomId  = it.roomId
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    networkState.value = State.OFFLINE
                }
                heaterDao.findHeatersByRoomId(roomId).map { it.toDto() }
            }
        }
        emit(elements)
    }

    fun createHeater(heater: HeaterDto) = liveData {
        val element: HeaterDto = withContext(Dispatchers.IO) {
            try {
                val response = ApiServices.heaterApi.createHeater(heater).execute()
                withContext(Dispatchers.Main) {
                    networkState.value = State.ONLINE
                }
                val heater: HeaterDto = response.body() ?: heater
                heater.apply {
                    heaterDao.create(
                        Heater(
                            id = heater.id,
                            name = heater.name,
                            power = heater.power,
                            heaterStatus = heater.heaterStatus,
                            roomId  = heater.roomId
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    networkState.value = State.OFFLINE
                }
                heater
            }
        }
        emit(element)
    }

    fun deleteHeater(id: Long) = liveData {
        val element: Boolean = withContext(Dispatchers.IO) {
            try {
                val response = ApiServices.heaterApi.deleteHeater(id).execute()
                withContext(Dispatchers.Main) {
                    networkState.value = State.ONLINE
                }
                val heater: Boolean = (response.body() ?: throw Exception("No Response")) as Boolean
                heater.apply {
                    heaterDao.deleteById(id)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    networkState.value = State.OFFLINE
                }
                false
            }
        }
        emit(element)
    }
}