package com.faircorp.api

import com.faircorp.dto.HeaterDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Used retrofit2
interface HeaterApi {
    // Get all heaters
    @GET("heaters")
    fun findAll(): Call<List<HeaterDto>>

    @GET("heaters/room/{room_id}")
    fun findHeatersByRoomId(@Path("room_id") room_id: Long): Call<List<HeaterDto>>
    // Switch heater ON or OFF
    @POST("heaters/{heater_id}/switch")
    fun switchStatus(@Path("heater_id") heater_id: Long): Call<HeaterDto>
    // Create heater
    @POST("heaters")
    fun createHeater(@Body HEATER: HeaterDto): Call<HeaterDto>
    // Delete heater by id
    @DELETE("heaters/{id}")
    fun deleteHeater(@Path("id") id: Long): Call<Void>
}