package com.faircorp.callbackFunctions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.faircorp.R
import com.faircorp.dto.BuildingDto
interface OnBuildingSelectedListener {
    fun onBuildingSelected(id: Long)
    fun onBuildingChange(id: Long)
}
class BuildingCallbackFunctions (private val listener : OnBuildingSelectedListener) : RecyclerView.Adapter<BuildingCallbackFunctions.BuildingViewHolder>() {

    inner class BuildingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.buildingView)
        val address : TextView = view.findViewById(R.id.addressView)
    }

    private val items = mutableListOf<BuildingDto>()

    fun update(buildings: List<BuildingDto>) {
        items.clear()
        items.addAll(buildings)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_buildings_list, parent, false)
        return BuildingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) {
        val building = items[position]
        holder.apply {
            name.text = building.name
            address.text = building.address
            itemView.setOnClickListener {listener.onBuildingSelected(building.id!!.toLong())}
            itemView.setOnLongClickListener {
                listener.onBuildingChange(building.id!!.toLong())
                true
            }
        }
    }
    override fun onViewRecycled(holder: BuildingViewHolder) { // (2)
        super.onViewRecycled(holder)
        holder.apply {
            itemView.setOnClickListener(null)
        }

    }
    fun getBuildingById(id: Long): BuildingDto? {
        return items.find { it.id == id }
    }
}