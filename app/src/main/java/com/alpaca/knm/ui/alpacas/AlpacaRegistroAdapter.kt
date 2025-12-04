package com.alpaca.knm.ui.alpacas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alpaca.knm.R
import com.alpaca.knm.domain.model.AlpacaRegistro

class AlpacaRegistroAdapter(
    private val onEditClick: (AlpacaRegistro) -> Unit
) : ListAdapter<AlpacaRegistro, AlpacaRegistroAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alpaca_registro, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvRaza: TextView = itemView.findViewById(R.id.tvRaza)
        private val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        private val tvAdultos: TextView = itemView.findViewById(R.id.tvAdultos)
        private val tvCrias: TextView = itemView.findViewById(R.id.tvCrias)
        private val tvTotal: TextView = itemView.findViewById(R.id.tvTotal)
        private val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)

        fun bind(registro: AlpacaRegistro) {
            tvRaza.text = registro.raza.name.lowercase().replaceFirstChar { it.uppercase() }
            tvFecha.text = registro.fechaRegistro ?: "Sin fecha"
            tvAdultos.text = registro.adultos.toString()
            tvCrias.text = registro.crias.toString()
            tvTotal.text = registro.cantidad.toString()
            
            btnEdit.setOnClickListener { onEditClick(registro) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<AlpacaRegistro>() {
        override fun areItemsTheSame(oldItem: AlpacaRegistro, newItem: AlpacaRegistro) = 
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: AlpacaRegistro, newItem: AlpacaRegistro) = 
            oldItem == newItem
    }
}
