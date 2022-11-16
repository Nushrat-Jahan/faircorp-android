package com.faircorp.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.faircorp.R
import com.faircorp.callbackFunctions.RoomCallbackFunctions
import com.faircorp.callbackFunctions.RoomListener
import com.faircorp.controller.MainController
import com.faircorp.controller.RoomController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomsActivity : MainActivity() , RoomListener {
    private val viewModel: RoomController by viewModels {
        RoomController.factory
    }
    var buildingId = 0L
    var adapter = RoomCallbackFunctions(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms)
        val recyclerView = findViewById<RecyclerView>(R.id.roomList)
        buildingId = intent.getLongExtra(BUILDING_ID, 0)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        val fab= findViewById<FloatingActionButton>(R.id.roomFloatingActionButton)
        fab.setOnClickListener {
            val intent= Intent(this, NewRoomActivity::class.java).putExtra(BUILDING_ID, buildingId)
            startActivity(intent)
        }

    }

    override fun onWindowClicked(id: Long) {
        val intent = Intent(this, WindowsActivity::class.java).putExtra(ROOM_ID, id)
        startActivity(intent)
    }

    override fun onHeaterClicked(id: Long) {
        val intent = Intent(this, HeatersActivity::class.java).putExtra(ROOM_ID, id)
        startActivity(intent)
    }

    override fun onRoomChange(id: Long) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.activity_edit_or_delete)
        dialog.show()
        dialog.findViewById<Button>(R.id.deleteBtn).setOnClickListener {

            viewModel.deleteRoom(id).observe(this) {

                lifecycleScope.launch(Dispatchers.IO) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RoomsActivity, "Room deleted", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                }
            }

        }
        dialog.findViewById<Button>(R.id.editBtn).setOnClickListener {
            val  room = adapter.getRoomById(id)
            val intent = Intent(this, NewRoomActivity::class.java)
                .putExtra(ROOM_ID, id)
                .putExtra(BUILDING_ID, buildingId)
                .putExtra(ROOM_NAME, room?.name)
                .putExtra(ROOM_FLOOR, room?.floor)
                .putExtra(ROOM_CURRENT_TEMPERATURE, room?.currentTemperature)
                .putExtra(ROOM_TARGET_TEMPERATURE, room?.targetTemperature)
            startActivity(intent)
            dialog.dismiss()
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.findByBuildingId(intent.getLongExtra(BUILDING_ID,0)).observe(this) { rooms ->
            adapter.update(rooms)
            viewModel.networkState.observe(this) { state ->
                if(state == MainController.State.OFFLINE) {
                    Toast.makeText(this,"Offline mode, the last known values are displayed", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

    }
}