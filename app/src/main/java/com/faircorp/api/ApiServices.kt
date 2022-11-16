package com.faircorp.api

import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


const val API_USERNAME = "user"
const val API_PASSWORD = "password"

object ApiServices {
    //authenticate to the server
    val credentials = Credentials.basic(API_USERNAME, API_PASSWORD)
    val interceptor = Interceptor { chain ->
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
            .header("Authorization", credentials).build()
        chain.proceed(authenticatedRequest)
    }
    val logging = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    val client = OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(logging)
        .build()
    val buildingApi : BuildingApi by lazy {

        Retrofit.Builder()
            .baseUrl("https://faircorp-nushrat-jahan.cleverapps.io/api/")
            .addConverterFactory(MoshiConverterFactory.create())

            .client(client)
            .build()
            .create(BuildingApi::class.java)
    }
    val roomApi : RoomApi by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://faircorp-nushrat-jahan.cleverapps.io/api/")
            .client(client)
            .build()
            .create(RoomApi::class.java)
    }
    val windowApiService : WindowApi by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://faircorp-nushrat-jahan.cleverapps.io/api/")
            .client(client)
            .build()
            .create(WindowApi::class.java)
    }
    val heaterApi : HeaterApi by lazy {
        Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .baseUrl("https://faircorp-nushrat-jahan.cleverapps.io/api/")
            .client(client)
            .build()
            .create(HeaterApi::class.java)
    }

}