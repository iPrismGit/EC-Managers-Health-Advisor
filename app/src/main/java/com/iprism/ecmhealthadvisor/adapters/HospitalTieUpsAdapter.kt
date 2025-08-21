package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.ecmhealthadvisor.databinding.InsuranceItemBinding

class HospitalTieUpsAdapter(var context: Context) : Adapter<HospitalTieUpsAdapter.HospitalTieUpViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HospitalTieUpsAdapter.HospitalTieUpViewHolder {
       var binding = InsuranceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HospitalTieUpViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HospitalTieUpsAdapter.HospitalTieUpViewHolder,
        position: Int
    ) {

    }

    override fun getItemCount(): Int {
       return 9
    }

    class HospitalTieUpViewHolder(var binding: InsuranceItemBinding) : ViewHolder(binding.root)

}