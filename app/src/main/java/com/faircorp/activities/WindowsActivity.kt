package com.faircorp.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.faircorp.R
import com.faircorp.callbackFunctions.WindowCallbackFunction
import com.faircorp.callbackFunctions.WindowListener
import com.faircorp.api.ApiServices
import com.faircorp.controller.MainController
import com.faircorp.controller.WindowController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WindowsActivity : AppCompatActivity(), WindowListener {
    private val viewModel: WindowController by viewModels {
        WindowController.factory
    }
    var roomId = 0L
    private val adapter = WindowCallbackFunction(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_windows)
        val recyclerView = findViewById<RecyclerView>(R.id.windowList)

        roomId = intent.getLongExtra(ROOM_ID, 0)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        val fab = findViewById<FloatingActionButton>(R.id.windowFloatingActionButton)
        fab.setOnClickListener {
            val intent = Intent(this, NewWindowActivity::class.java).putExtra(ROOM_ID, roomId)
            startActivity(intent)
        }


    }

    override fun onWindowSwitched(id: Long) {
        lifecycleScope.launch(Dispatchers.IO) {
            runCatching { ApiServices.windowApiService.switchStatus(id).execute() }
                .onSuccess {
                    withContext(context = Dispatchers.Main) {
                        Toast.makeText(
                            applicationContext,
                            "Window switched",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                .onFailure {
                    withContext(context = Dispatchers.Main) {
                        Toast.makeText(
                            applicationContext,
                            "Error on switching window $it",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    override fun onWindowChange(id: Long) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.activity_edit_or_delete)
        dialog.show()
        dialog.findViewById<Button>(R.id.deleteBtn).setOnClickListener {

            viewModel.deleteWindow(id).observe(this) {

                lifecycleScope.launch(Dispatchers.IO) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@WindowsActivity, "Window deleted", Toast.LENGTH_LONG)
                            .show()
                        dialog.dismiss()
                    }
                }
            }

        }
        dialog.findViewById<Button>(R.id.editBtn).setOnClickListener {
            val window = adapter.getWindowById(id)
            val intent =
                Intent(this, NewWindowActivity::class.java)
                    .putExtra(WINDOW_ID, id).putExtra(ROOM_ID, roomId)
                    .putExtra(Window_NAME, window?.name)
                    .putExtra(Window_STATUS, window?.windowStatus.toString())
            startActivity(intent)
            dialog.dismiss()
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.findByRoomId(intent.getLongExtra(ROOM_ID, 0)).observe(this) { windows ->
            adapter.update(windows)
            viewModel.networkState.observe(this) { state ->
                if (state == MainController.State.OFFLINE) {
                    Toast.makeText(
                        this,
                        "Offline mode, the last known values are displayed",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            }
        }
    }
}