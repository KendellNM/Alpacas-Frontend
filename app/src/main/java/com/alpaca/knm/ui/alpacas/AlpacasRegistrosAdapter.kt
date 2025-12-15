package com.alpaca.knm.ui.alpacas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alpaca.knm.R
import com.alpaca.knm.data.remote.dto.AlpacaRegistroResponse

class AlpacasRegistrosAdapter : ListAdapter<AlpacaRegistroResponse, AlpacasRegistrosAdapter.ViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alpaca_registro_admin, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvGanaderoId: TextView = itemView.findViewById(R.id.tvGanaderoId)
        private val tvRaza: TextView = itemView.findViewById(R.id.tvRaza)
        private val tvCantidad: TextView = itemView.findViewById(R.id.tvCantidad)
        private val tvAdultos: TextView = itemView.findViewById(R.id.tvAdultos)
        private val tvCrias: TextView = itemView.findViewById(R.id.tvCrias)
        
        fun bind(registro: AlpacaRegistroResponse) {
            tvGanaderoId.text = if (registro.ganaderoNombre.isNotEmpty()) registro.ganaderoNombre else "Ganadero #${registro.ganaderoId}"
            tvRaza.text = registro.raza
            tvCantidad.text = "Total: ${registro.cantidad}"
            tvAdultos.text = "Adultos: ${registro.adultos}"
            tvCrias.text = "Crias: ${registro.crias}"
        }
    }
    
    class DiffCallback : DiffUtil.ItemCallback<AlpacaRegistroResponse>() {
        override fun areItemsTheSame(oldItem: AlpacaRegistroResponse, newItem: AlpacaRegistroResponse) = 
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: AlpacaRegistroResponse, newItem: AlpacaRegistroResponse) = 
            oldItem == newItem
    }
}
