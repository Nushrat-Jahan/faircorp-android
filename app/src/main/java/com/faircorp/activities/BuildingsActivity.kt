package com.faircorp.activities

import android.annotation.SuppressLint
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
import com.faircorp.callbackFunctions.BuildingCallbackFunctions
import com.faircorp.callbackFunctions.OnBuildingSelectedListener
import com.faircorp.controller.MainController
import com.faircorp.controller.BuildingController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BuildingsActivity : MainActivity(),OnBuildingSelectedListener {
    private val viewModel: BuildingController by viewModels {
        BuildingController.factory
    }
    val adapter = BuildingCallbackFunctions(this)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.buildingList)

        val fab = findViewById<FloatingActionButton>(R.id.CornerAddButton)
        fab.setOnClickListener {

            val intent= Intent(this, NewBuildingActivity::class.java)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

    }
    override fun onBuildingSelected(id: Long) {
        val intent = Intent(this, RoomsActivity::class.java).putExtra(BUILDING_ID, id)
        startActivity(intent)
    }

    override fun onBuildingChange(id: Long) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.activity_edit_or_delete)
        dialog.show()
        dialog.findViewById<Button>(R.id.deleteBtn).setOnClickListener {

            viewModel.deleteBuilding(id).observe(this) {

                lifecycleScope.launch(Dispatchers.IO) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@BuildingsActivity, "Building deleted", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                }
            }

        }
        dialog.findViewById<Button>(R.id.editBtn).setOnClickListener {
            val building =adapter.getBuildingById(id)
            val intent = Intent(this, NewBuildingActivity::class.java).putExtra(BUILDING_ID, id)
                .putExtra(BUILDING_NAME, building?.name).putExtra(
                    BUILDING_ADDRESS, building?.address
                )
            startActivity(intent)
            dialog.dismiss()
        }



    }

    override fun onResume() {
        super.onResume()
        viewModel.findAll().observe(this) { buildings ->
            adapter.update(buildings)
            viewModel.networkState.observe(this) { state ->
                if(state == MainController.State.OFFLINE) {
                    Toast.makeText(this,"Offline mode, the last known values are displayed", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }

    }

}