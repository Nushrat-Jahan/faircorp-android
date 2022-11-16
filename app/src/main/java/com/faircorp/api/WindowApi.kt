package com.faircorp.api

import com.faircorp.dto.WindowDto
import retrofit2.Call
import retrofit2.http.*

interface WindowApi {
    // Get all windows
    @GET("windows")
    fun findAll(): Call<List<WindowDto>>

    // Find window by room id
    @GET("windows/room/{room_id}")
    fun findWindowsByRoomId(@Path("room_id") room_id: Long): Call<List<WindowDto>>

    // Switch window OPEN or CLOSE
    @POST("windows/{window_id}/switch")
    fun switchStatus(@Path("window_id") window_id: Long): Call<WindowDto>

    // Create window
    @POST("windows")
    fun createWindow(@Body window: WindowDto): Call<WindowDto>

    // Delete window by id
    @DELETE("windows/{window_id}")
    fun deleteById(@Path("window_id") window_id: Long): Call<Void>
}