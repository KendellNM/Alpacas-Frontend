package com.alpaca.knm.presentation.solicitudes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alpaca.knm.R
import com.alpaca.knm.domain.model.Solicitud
import com.alpaca.knm.domain.model.SolicitudStatus
import com.google.android.material.button.MaterialButton
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class SolicitudesAdapter(
    private val onApprove: (Solicitud) -> Unit,
    private val onReject: (Solicitud) -> Unit
) : RecyclerView.Adapter<SolicitudesAdapter.SolicitudViewHolder>() {
    
    private var solicitudes = listOf<Solicitud>()
    
    fun submitList(newList: List<Solicitud>) {
        val diffCallback = SolicitudDiffCallback(solicitudes, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        solicitudes = newList
        diffResult.dispatchUpdatesTo(this)
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolicitudViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_solicitud, parent, false)
        return SolicitudViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: SolicitudViewHolder, position: Int) {
        holder.bind(solicitudes[position])
    }
    
    override fun getItemCount() = solicitudes.size
    
    inner class SolicitudViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvSolicitudId: TextView? = itemView.findViewById(R.id.tvSolicitudId)
        private val tvGanaderoName: TextView = itemView.findViewById(R.id.tvGanaderoName)
        private val tvKilograms: TextView = itemView.findViewById(R.id.tvKilograms)
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        private val tvDate: TextView? = itemView.findViewById(R.id.tvDate)
        private val tvRecommendation: TextView? = itemView.findViewById(R.id.tvRecommendation)
        private val layoutActions: LinearLayout? = itemView.findViewById(R.id.layoutActions)
        private val btnApprove: MaterialButton = itemView.findViewById(R.id.btnApprove)
        private val btnReject: MaterialButton = itemView.findViewById(R.id.btnReject)
        
        fun bind(solicitud: Solicitud) {
            tvSolicitudId?.text = "SOLICITUD #${solicitud.id}"
            
            tvGanaderoName.text = solicitud.ganaderoNombre
            
            val scoring = solicitud.scoring ?: 50
            tvKilograms.text = "Scoring: $scoring | ${String.format("%.2f", solicitud.kilograms)} KG"
            
            val currencyFormat = NumberFormat.getCurrencyInstance(Locale("es", "PE"))
            tvAmount.text = currencyFormat.format(solicitud.totalAmount)
            
            tvDate?.let {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                it.text = dateFormat.format(solicitud.requestDate)
            }
            
            tvRecommendation?.let {
                if (scoring >= 60) {
                    it.text = "Rec: APROBAR"
                    it.setTextColor(itemView.context.getColor(R.color.success))
                } else {
                    it.text = "Rec: REVISAR"
                    it.setTextColor(itemView.context.getColor(R.color.danger))
                }
            }
            
            when (solicitud.status) {
                SolicitudStatus.PENDIENTE -> {
                    tvStatus.text = "PENDIENTE"
                    tvStatus.setBackgroundResource(R.drawable.bg_status_pending)
                    layoutActions?.visibility = View.VISIBLE
                    btnApprove.visibility = View.VISIBLE
                    btnReject.visibility = View.VISIBLE
                }
                SolicitudStatus.APROBADA -> {
                    tvStatus.text = "APROBADA"
                    tvStatus.setBackgroundResource(R.drawable.bg_status_approved)
                    layoutActions?.visibility = View.GONE
                    btnApprove.visibility = View.GONE
                    btnReject.visibility = View.GONE
                }
                SolicitudStatus.RECHAZADA -> {
                    tvStatus.text = "RECHAZADA"
                    tvStatus.setBackgroundResource(R.drawable.bg_status_rejected)
                    layoutActions?.visibility = View.GONE
                    btnApprove.visibility = View.GONE
                    btnReject.visibility = View.GONE
                }
                SolicitudStatus.DESEMBOLSADA -> {
                    tvStatus.text = "DESEMBOLSADA"
                    tvStatus.setBackgroundResource(R.drawable.bg_status_approved)
                    layoutActions?.visibility = View.GONE
                    btnApprove.visibility = View.GONE
                    btnReject.visibility = View.GONE
                }
            }
            
            btnApprove.setOnClickListener { onApprove(solicitud) }
            btnReject.setOnClickListener { onReject(solicitud) }
        }
    }
    
    private class SolicitudDiffCallback(
        private val oldList: List<Solicitud>,
        private val newList: List<Solicitud>
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
