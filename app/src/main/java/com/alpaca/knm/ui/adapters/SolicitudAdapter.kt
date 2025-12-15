package com.alpaca.knm.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alpaca.knm.R
import com.alpaca.knm.data.remote.dto.SolicitudDto

class SolicitudAdapter : RecyclerView.Adapter<SolicitudAdapter.ViewHolder>() {
    
    private val items = mutableListOf<SolicitudDto>()
    
    fun submitList(list: List<SolicitudDto>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mi_solicitud, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
    
    override fun getItemCount() = items.size
    
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvMonto: TextView = view.findViewById(R.id.tvMonto)
        private val tvFecha: TextView = view.findViewById(R.id.tvFecha)
        private val tvEstado: TextView = view.findViewById(R.id.tvEstado)
        
        fun bind(item: SolicitudDto) {
            tvMonto.text = "S/ %.2f".format(item.totalAmount)
            tvFecha.text = item.requestDate?.take(10) ?: ""
            tvEstado.text = item.status
            
            when (item.status) {
                "PENDIENTE" -> {
                    tvEstado.setBackgroundColor(Color.parseColor("#FFF3CD"))
                    tvEstado.setTextColor(Color.parseColor("#856404"))
                }
                "APROBADA" -> {
                    tvEstado.setBackgroundColor(Color.parseColor("#D4EDDA"))
                    tvEstado.setTextColor(Color.parseColor("#155724"))
                }
                "RECHAZADA" -> {
                    tvEstado.setBackgroundColor(Color.parseColor("#F8D7DA"))
                    tvEstado.setTextColor(Color.parseColor("#721C24"))
                }
            }
        }
    }
}
