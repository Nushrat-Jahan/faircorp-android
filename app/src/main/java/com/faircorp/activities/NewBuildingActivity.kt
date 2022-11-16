package com.faircorp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import com.faircorp.R
import com.faircorp.dto.BuildingDto
import com.faircorp.controller.BuildingController


class NewBuildingActivity : AppCompatActivity() {
    private val viewModel: BuildingController by viewModels {
        BuildingController.factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_building)
        val editTextBuildingName = findViewById<EditText>(R.id.newBuildingName)
        val editTextBuildingAddress = findViewById<EditText>(R.id.newBuildingAddress)
        val submitButton = findViewById<Button>(R.id.saveBuildingBtn)
        val id = intent.getLongExtra(BUILDING_ID, 0)
        if (intent.getStringExtra(BUILDING_NAME) != null) {
            editTextBuildingName.setText(intent.getStringExtra(BUILDING_NAME))
            editTextBuildingAddress.setText(intent.getStringExtra(BUILDING_ADDRESS))
        }
        submitButton.setOnClickListener {
            val buildingName = editTextBuildingName.text.toString()
            val buildingAddress = editTextBuildingAddress.text.toString()
            if (id != 0L) {
                viewModel.createBuilding(BuildingDto(id=id, name = buildingName, address = buildingAddress))
                    .observe(this) {
                    Toast.makeText(this, "Building is Updated", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
            else {
                viewModel.createBuilding(BuildingDto(name = buildingName, address = buildingAddress))
                    .observe(this) {
                    Toast.makeText(this, "Building is created", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

}