package com.example.smartvipparking.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartvipparking.R
import com.example.smartvipparking.model.ParkingHistory
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(private val historyList: List<ParkingHistory>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvVehicle: TextView = view.findViewById(R.id.tv_vehicle_number)
        val tvStatus: TextView = view.findViewById(R.id.tv_status)
        val tvSpot: TextView = view.findViewById(R.id.tv_spot)
        val tvEntry: TextView = view.findViewById(R.id.tv_entry_time)
        val tvAmount: TextView = view.findViewById(R.id.tv_amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_parking_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = historyList[position]
        val sdf = SimpleDateFormat("hh:mm a, dd MMM", Locale.getDefault())
        
        holder.tvVehicle.text = item.vehicleNumber
        holder.tvStatus.text = item.status
        holder.tvSpot.text = "Spot: ${item.parkingSpot}"
        holder.tvEntry.text = "Entry: ${sdf.format(Date(item.entryTime))}"
        holder.tvAmount.text = "Rs. ${String.format("%.2f", item.amountPaid)}"
        
        if (item.status == "Active") {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_active)
        } else {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_status_completed)
        }
    }

    override fun getItemCount() = historyList.size
}
