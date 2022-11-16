package com.faircorp.api

import com.faircorp.dto.BuildingDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BuildingApi {
    // Get all buildings
    @GET("buildings")
    fun findAll(): Call<List<BuildingDto>>

    @GET("buildings/{id}")
    fun findById(@Path("id") id: Long): Call<BuildingDto>

    @POST("buildings")
    fun createBuilding(@Body BUILDING: BuildingDto): Call<BuildingDto>

    // Delete building by id
    @DELETE("buildings/{id}")
    fun deleteBuilding(@Path("id") id: Long): Call<Void>
}