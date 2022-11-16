package com.faircorp.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.activity.viewModels
import com.faircorp.R
import com.faircorp.dto.HeaterDto
import com.faircorp.dto.HeaterStatus
import com.faircorp.controller.HeaterController

class NewHeaterActivity : MainActivity() {
    private val viewModel: HeaterController by viewModels {
        HeaterController.factory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_heater)
        val id = intent.getLongExtra(HEATER_ID, 0)
        val roomId = intent.getLongExtra(ROOM_ID, 0)
        val editTextName = findViewById<EditText>(R.id.newHeaterName)
        val editTextPower = findViewById<EditText>(R.id.newHeaterPower)
        val radioStatus = findViewById<RadioGroup>(R.id.radioGroupHeaterStatus)
        val saveButton = findViewById<Button>(R.id.saveHeaterbtn)
        if (intent.getStringExtra(HEATER_NAME) != null) {
            editTextName.setText(intent.getStringExtra(HEATER_NAME))
            editTextPower.setText(intent.getLongExtra(HEATER_POWER,0).toString())
            if (intent.getStringExtra(HEATER_STATUS) == HeaterStatus.ON.name) {
                radioStatus.check(R.id.radioButtonHeaterOn)
            } else {
                radioStatus.check(R.id.radioButtonHeaterOff)
            }
        }
        saveButton.setOnClickListener {
            val name = editTextName.text.toString()
            val power = editTextPower.text.toString().toLongOrNull()
            val status = when (radioStatus.checkedRadioButtonId) {
                R.id.radioButtonHeaterOn -> HeaterStatus.ON
                R.id.radioButtonHeaterOff -> HeaterStatus.OFF
                else -> HeaterStatus.OFF
            }
            viewModel.createHeater(
                HeaterDto(
                    id = if (id == 0L) null else id,
                    name = name,
                    power = power,
                    heaterStatus = status,
                    roomId = roomId
                )
            )
                .observe(this) {
                    finish()
                }
        }
    }

}