package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.ecmhealthadvisor.R

import com.iprism.ecmhealthadvisor.databinding.InsuranceItemBinding

class HospitalFecilitiesAdapter(var context : Context) : Adapter<HospitalFecilitiesAdapter.HospitalFecilityViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HospitalFecilitiesAdapter.HospitalFecilityViewHolder {
        var binding = InsuranceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HospitalFecilityViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HospitalFecilitiesAdapter.HospitalFecilityViewHolder,
        position: Int
    ) {
        holder.binding.imageView6.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.img))
    }

    override fun getItemCount(): Int {
        return 6
    }

    class HospitalFecilityViewHolder(var binding : InsuranceItemBinding): ViewHolder(binding.root)

}