package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.ecmhealthadvisor.databinding.NotificationItemBinding

class NotificationsAdapter(private var context: Context) : Adapter<NotificationsAdapter.NotificationViewHolder> (){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationsAdapter.NotificationViewHolder {
        var binding = NotificationItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: NotificationsAdapter.NotificationViewHolder,
        position: Int
    ) {

    }

    override fun getItemCount(): Int {
        return 10
    }

    class NotificationViewHolder(var binding: NotificationItemBinding):ViewHolder(binding.root)

}