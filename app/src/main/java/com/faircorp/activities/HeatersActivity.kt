package com.faircorp.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
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
import com.faircorp.callbackFunctions.HeaterCallbackFunctions
import com.faircorp.callbackFunctions.HeaterListener
import com.faircorp.api.ApiServices
import com.faircorp.controller.MainController
import com.faircorp.controller.HeaterController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HeatersActivity : AppCompatActivity(),HeaterListener{
    private val viewModel: HeaterController by viewModels {
        HeaterController.factory
    }
    private var roomID:Long = 0
    private val adapter = HeaterCallbackFunctions(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heaters)
        roomID= intent.getLongExtra(ROOM_ID,0)
        val recyclerView = findViewById<RecyclerView>(R.id.heaterList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        val fab = findViewById<FloatingActionButton>(R.id.heaterFloatingActionButton)
        fab.setOnClickListener {
            val intent = Intent(this, NewHeaterActivity::class.java).putExtra(ROOM_ID, roomID)
            startActivity(intent)
        }




    }

    override fun onHeaterSwitched(id: Long) {

        lifecycleScope.launch(Dispatchers.IO) {
            runCatching { ApiServices.heaterApi.switchStatus(id).execute() }
                .onSuccess {
                    withContext(context = Dispatchers.Main) {
                        Toast.makeText(
                            applicationContext,
                            "Heater switched",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                .onFailure {
                    withContext(context = Dispatchers.Main) {
                        Toast.makeText(
                            applicationContext,
                            "Error on heater switching $it",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    override fun onHeaterChange(id: Long) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.activity_edit_or_delete)
        dialog.show()
        dialog.findViewById<Button>(R.id.deleteBtn).setOnClickListener {

            viewModel.deleteHeater(id).observe(this) {

                lifecycleScope.launch(Dispatchers.IO) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@HeatersActivity, "Heater deleted", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                }
            }

        }
        dialog.findViewById<Button>(R.id.editBtn).setOnClickListener {
            val heater =adapter.getHeaterById(id)
            val intent = Intent(this, NewHeaterActivity::class.java)
                .putExtra(HEATER_ID, id)
                .putExtra(ROOM_ID,roomID)
                .putExtra(HEATER_NAME,heater?.name)
                .putExtra(HEATER_POWER, heater?.power)
                .putExtra(HEATER_STATUS, heater?.heaterStatus.toString())
            startActivity(intent)
            dialog.dismiss()
        }



    }

    override fun onResume() {
        super.onResume()
        viewModel.findHeatersByRoomId(intent.getLongExtra(ROOM_ID,0)).observe(this) { heaters ->
            adapter.update(heaters)
            viewModel.networkState.observe(this) { state ->
                if(state == MainController.State.OFFLINE) {
                    Toast.makeText(this,"Offline mode", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }
}