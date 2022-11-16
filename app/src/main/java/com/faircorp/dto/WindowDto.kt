package com.faircorp.dto

data class WindowDto(
    val id: Long?=null,
    val name: String,
    val roomId:Long,
    val roomName:String="",
    val windowStatus: WindowStatus
    )
