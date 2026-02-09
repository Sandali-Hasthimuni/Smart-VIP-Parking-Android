package com.example.smartvipparking.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartvipparking.R
import com.example.smartvipparking.model.Vehicle

class VehicleAdapter(private val vehicles: List<Vehicle>) :
    RecyclerView.Adapter<VehicleAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNumber: TextView = view.findViewById(R.id.tv_vehicle_number)
        val tvModel: TextView = view.findViewById(R.id.tv_vehicle_model)
        val tvType: TextView = view.findViewById(R.id.tv_vehicle_type)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vehicle, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val v = vehicles[position]
        holder.tvNumber.text = v.vehicleNumber
        holder.tvModel.text = v.model
        holder.tvType.text = v.type
    }

    override fun getItemCount() = vehicles.size
}
