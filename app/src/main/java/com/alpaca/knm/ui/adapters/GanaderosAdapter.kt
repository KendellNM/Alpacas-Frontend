package com.alpaca.knm.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alpaca.knm.R
import com.alpaca.knm.domain.model.Ganadero

class GanaderosAdapter(
    private val onItemClick: (Ganadero) -> Unit,
    private val onDeleteClick: (Ganadero) -> Unit
) : ListAdapter<Ganadero, GanaderosAdapter.GanaderoViewHolder>(GanaderoDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GanaderoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_ganadero, parent, false)
        return GanaderoViewHolder(view)
    }

    override fun onBindViewHolder(holder: GanaderoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GanaderoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tvName)
        private val tvDni: TextView = itemView.findViewById(R.id.tvDni)
        private val tvLocation: TextView? = itemView.findViewById(R.id.tvLocation)
        private val tvAlpacasCount: TextView? = itemView.findViewById(R.id.tvAlpacasCount)
        private val btnDelete: ImageButton? = itemView.findViewById(R.id.btnDelete)
        private val tvStatus: TextView? = itemView.findViewById(R.id.tvStatus)
        private val tvScoring: TextView? = itemView.findViewById(R.id.tvScoring)
        private val ivAvatar: ImageView? = itemView.findViewById(R.id.ivAvatar)

        fun bind(ganadero: Ganadero) {
            tvName.text = ganadero.fullName
            
            // DNI y comunidad en una línea
            val comunidad = ganadero.district.ifEmpty { ganadero.province }
            tvDni.text = "DNI: ${ganadero.dni} | $comunidad"
            
            // Location (si existe en el layout antiguo)
            tvLocation?.text = "${ganadero.district}, ${ganadero.province}, ${ganadero.department}"
            
            // Alpacas count (si existe)
            tvAlpacasCount?.text = itemView.context.getString(
                R.string.ganaderos_alpacas,
                ganadero.alpacasCount
            )
            
            // Status badge
            tvStatus?.let {
                val status = ganadero.status ?: "activo"
                it.text = status.uppercase()
                when (status.lowercase()) {
                    "activo" -> it.setBackgroundResource(R.drawable.bg_status_active)
                    "inactivo" -> it.setBackgroundResource(R.drawable.bg_status_inactive)
                    "suspendido" -> it.setBackgroundResource(R.drawable.bg_status_suspended)
                    else -> it.setBackgroundResource(R.drawable.bg_status_active)
                }
            }
            
            // Scoring
            tvScoring?.text = (ganadero.scoring ?: 50).toString()
            
            // Avatar color based on status
            ivAvatar?.let {
                val status = ganadero.status ?: "activo"
                when (status.lowercase()) {
                    "activo" -> it.setBackgroundResource(R.drawable.bg_status_active)
                    "inactivo" -> it.setBackgroundResource(R.drawable.bg_status_inactive)
                    "suspendido" -> it.setBackgroundResource(R.drawable.bg_status_suspended)
                    else -> it.setBackgroundResource(R.drawable.bg_status_active)
                }
            }

            itemView.setOnClickListener {
                onItemClick(ganadero)
            }

            btnDelete?.setOnClickListener {
                onDeleteClick(ganadero)
            }
        }
    }

    private class GanaderoDiffCallback : DiffUtil.ItemCallback<Ganadero>() {
        override fun areItemsTheSame(oldItem: Ganadero, newItem: Ganadero): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Ganadero, newItem: Ganadero): Boolean {
            return oldItem == newItem
        }
    }
}
