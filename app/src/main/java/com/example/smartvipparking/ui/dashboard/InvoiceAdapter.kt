package com.example.smartvipparking.ui.dashboard

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smartvipparking.R
import com.example.smartvipparking.model.Invoice
import java.text.SimpleDateFormat
import java.util.*

class InvoiceAdapter(private val invoices: List<Invoice>) :
    RecyclerView.Adapter<InvoiceAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDesc: TextView = view.findViewById(R.id.tv_invoice_desc)
        val tvDate: TextView = view.findViewById(R.id.tv_invoice_date)
        val tvAmount: TextView = view.findViewById(R.id.tv_invoice_amount)
        val tvStatus: TextView = view.findViewById(R.id.tv_invoice_status)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_invoice, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = invoices[position]
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        
        holder.tvDesc.text = item.description
        holder.tvDate.text = sdf.format(Date(item.date))
        holder.tvAmount.text = "Rs. ${String.format("%.2f", item.amount)}"
        holder.tvStatus.text = item.status
        
        if (item.status == "Paid") {
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"))
        } else {
            holder.tvStatus.setTextColor(Color.parseColor("#F44336"))
        }
    }

    override fun getItemCount() = invoices.size
}
