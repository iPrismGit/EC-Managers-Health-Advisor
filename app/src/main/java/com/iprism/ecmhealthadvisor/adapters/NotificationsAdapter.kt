package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.ecmhealthadvisor.databinding.NotificationItemBinding
import com.iprism.ecmhealthadvisor.modals.notification.Notification

class NotificationsAdapter(private var context: Context, private var notifications : List<Notification>) : Adapter<NotificationsAdapter.NotificationViewHolder> (){
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
        var notification = notifications[position]
        holder.binding.titleTxt.text = notification.title
        holder.binding.dateTxt.text = notification.created_on
        holder.binding.messageTxt.text = notification.message
    }

    override fun getItemCount(): Int {
        return notifications.size
    }

    class NotificationViewHolder(var binding: NotificationItemBinding):ViewHolder(binding.root)

}