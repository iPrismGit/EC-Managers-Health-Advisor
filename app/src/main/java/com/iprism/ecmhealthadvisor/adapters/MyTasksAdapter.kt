package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.ecmcorporatemarketing.interfaces.OnDoctorItemClickListener
import com.iprism.ecmhealthadvisor.databinding.DoctorItemBinding

class MyTasksAdapter(var context: Context) : Adapter<MyTasksAdapter.MyTaskViewHolder>() {

    private lateinit var listener: OnDoctorItemClickListener

    fun setupListener(listener: OnDoctorItemClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyTasksAdapter.MyTaskViewHolder {
        var binding = DoctorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyTaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyTasksAdapter.MyTaskViewHolder, position: Int) {
        holder.binding.categoryName.text = "My Leads"
        holder.binding.root.setOnClickListener(View.OnClickListener {
            listener.onItemClick("", "")
        })
    }

    override fun getItemCount(): Int {
        return 10
    }

    class MyTaskViewHolder(var binding: DoctorItemBinding) : ViewHolder(binding.root)
}