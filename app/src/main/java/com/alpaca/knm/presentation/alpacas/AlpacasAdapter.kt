package com.alpaca.knm.presentation.alpacas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alpaca.knm.R
import com.alpaca.knm.domain.model.Alpaca

class AlpacasAdapter(
    private val onEdit: (Alpaca) -> Unit,
    private val onDelete: (Alpaca) -> Unit
) : RecyclerView.Adapter<AlpacasAdapter.AlpacaViewHolder>() {
    
    private var alpacas = listOf<Alpaca>()
    
    fun submitList(newList: List<Alpaca>) {
        val diffCallback = AlpacaDiffCallback(alpacas, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        alpacas = newList
        diffResult.dispatchUpdatesTo(this)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlpacaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_alpaca, parent, false)
        return AlpacaViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: AlpacaViewHolder, position: Int) {
        holder.bind(alpacas[position])
    }
    
    override fun getItemCount() = alpacas.size
    
    inner class AlpacaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvAlpacaNombre)
        private val tvRaza: TextView = itemView.findViewById(R.id.tvAlpacaRaza)
        private val tvColor: TextView = itemView.findViewById(R.id.tvAlpacaColor)
        private val tvEdad: TextView = itemView.findViewById(R.id.tvAlpacaEdad)
        private val tvPeso: TextView = itemView.findViewById(R.id.tvAlpacaPeso)
        private val tvSexo: TextView = itemView.findViewById(R.id.tvAlpacaSexo)
        private val tvEstado: TextView = itemView.findViewById(R.id.tvAlpacaEstado)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDeleteAlpaca)
        
        fun bind(alpaca: Alpaca) {
            tvNombre.text = alpaca.nombre
            tvRaza.text = alpaca.raza.name
            tvColor.text = alpaca.color
            tvEdad.text = "${alpaca.edad} años"
            tvPeso.text = "${alpaca.peso} kg"
            tvSexo.text = alpaca.sexo.name
            tvEstado.text = alpaca.estado.name
            
            // Color del estado
            when (alpaca.estado) {
                com.alpaca.knm.domain.model.AlpacaEstado.ACTIVO -> {
                    tvEstado.setBackgroundResource(R.drawable.bg_status_approved)
                }
                com.alpaca.knm.domain.model.AlpacaEstado.VENDIDO -> {
                    tvEstado.setBackgroundResource(R.drawable.bg_status_pending)
                }
                com.alpaca.knm.domain.model.AlpacaEstado.FALLECIDO -> {
                    tvEstado.setBackgroundResource(R.drawable.bg_status_rejected)
                }
            }
            
            itemView.setOnClickListener { onEdit(alpaca) }
            btnDelete.setOnClickListener { onDelete(alpaca) }
        }
    }
    
    private class AlpacaDiffCallback(
        private val oldList: List<Alpaca>,
        private val newList: List<Alpaca>
    ) : DiffUtil.Callback() {
        
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }
        
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
