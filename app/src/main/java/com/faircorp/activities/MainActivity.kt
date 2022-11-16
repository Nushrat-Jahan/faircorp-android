package com.faircorp.activities

import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.faircorp.R


// BUILDING
val BUILDING_ID = "building_id"
val BUILDING_NAME = "building_name"
val BUILDING_ADDRESS = "building_address"

// ROOM
val ROOM_ID = "room_id"
val ROOM_NAME = "room_name"
val ROOM_FLOOR = "room_floor"
val ROOM_TARGET_TEMPERATURE = "room_target_temperature"
val ROOM_CURRENT_TEMPERATURE = "room_current_temperature"

// WINDOW
val WINDOW_ID= "window_id"
val Window_NAME = "window_name"
val Window_STATUS = "window_status"

// HEATER
val HEATER_ID = "heater_id"
val HEATER_NAME = "heater_name"
val HEATER_POWER = "heater_power"
val HEATER_STATUS = "heater_status"


// For menu item
open class MainActivity : AppCompatActivity() {
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.menu_website -> startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("https://faircorp-nushrat-jahan.cleverapps.io"))
            )
            R.id.menu_email -> startActivity(
                Intent(Intent.ACTION_SENDTO, Uri.parse("mailto://nushratjahan296@gmail.com"))
            )

        }
        return super.onContextItemSelected(item)
    }

}