package com.alpaca.knm.ui.alpacas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alpaca.knm.R

class AlpacasRegistrosAgrupadorAdapter : ListAdapter<GanaderoConAlpacas, AlpacasRegistrosAgrupadorAdapter.ViewHolder>(DiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ganadero_alpacas, parent, false)
        return ViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvGanaderoNombre: TextView = itemView.findViewById(R.id.tvGanaderoNombre)
        private val tvTotalAlpacas: TextView = itemView.findViewById(R.id.tvTotalAlpacas)
        private val tvAdultos: TextView = itemView.findViewById(R.id.tvAdultos)
        private val tvCrias: TextView = itemView.findViewById(R.id.tvCrias)
        private val layoutRegistros: LinearLayout = itemView.findViewById(R.id.layoutRegistros)
        
        fun bind(item: GanaderoConAlpacas) {
            tvGanaderoNombre.text = item.ganaderoNombre
            tvTotalAlpacas.text = "${item.totalAlpacas} alpacas"
            tvAdultos.text = "Adultos: ${item.totalAdultos}"
            tvCrias.text = "Crias: ${item.totalCrias}"
            
            layoutRegistros.removeAllViews()
            
            item.registros.forEach { registro ->
                val registroView = LayoutInflater.from(itemView.context)
                    .inflate(R.layout.item_alpaca_raza_simple, layoutRegistros, false)
                
                registroView.findViewById<TextView>(R.id.tvRaza).text = registro.raza
                registroView.findViewById<TextView>(R.id.tvCantidad).text = "${registro.cantidad}"
                registroView.findViewById<TextView>(R.id.tvDetalle).text = "(${registro.adultos} adultos, ${registro.crias} crias)"
                
                layoutRegistros.addView(registroView)
            }
        }
    }
    
    class DiffCallback : DiffUtil.ItemCallback<GanaderoConAlpacas>() {
        override fun areItemsTheSame(oldItem: GanaderoConAlpacas, newItem: GanaderoConAlpacas) = 
            oldItem.ganaderoNombre == newItem.ganaderoNombre
        override fun areContentsTheSame(oldItem: GanaderoConAlpacas, newItem: GanaderoConAlpacas) = 
            oldItem == newItem
    }
}
