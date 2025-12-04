package com.alpaca.knm.ui.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alpaca.knm.R
import com.alpaca.knm.data.remote.dto.UserDto

class UsersAdapter(
    private val users: List<UserDto>,
    private val onEdit: (UserDto) -> Unit,
    private val onDelete: (UserDto) -> Unit
) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }
    
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }
    
    override fun getItemCount() = users.size
    
    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUsername: TextView = itemView.findViewById(R.id.tvUsername)
        private val tvFullName: TextView = itemView.findViewById(R.id.tvFullName)
        private val tvRole: TextView = itemView.findViewById(R.id.tvRole)
        private val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
        
        fun bind(user: UserDto) {
            tvUsername.text = user.username
            tvFullName.text = user.fullName ?: user.email ?: "-"
            tvRole.text = if (user.role == "ROLE_ADMIN") "Administrador" else "Ganadero"
            
            btnEdit.setOnClickListener { onEdit(user) }
            btnDelete.setOnClickListener { onDelete(user) }
        }
    }
}
