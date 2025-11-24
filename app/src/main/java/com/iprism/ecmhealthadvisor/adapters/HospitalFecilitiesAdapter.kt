package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.ecmhealthadvisor.databinding.FacilityItemBinding

import com.iprism.ecmhealthadvisor.modals.hospitalmodels.Facility

class HospitalFecilitiesAdapter(var context: Context, var facilities: List<Facility>) :
    Adapter<HospitalFecilitiesAdapter.HospitalFecilityViewHolder>(),
    FacilitiesInnerAdapter.OnFacilityInnerClickListener {

    private lateinit var listener: OnFacilityOuterClickListener


    fun setupListener(listener: OnFacilityOuterClickListener) {
        this.listener = listener
    }

    interface OnFacilityOuterClickListener {
        fun onItemClick(url: String, type: String)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HospitalFecilitiesAdapter.HospitalFecilityViewHolder {
        var binding =
            FacilityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HospitalFecilityViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HospitalFecilitiesAdapter.HospitalFecilityViewHolder,
        position: Int
    ) {
        var facility = facilities[position]
        holder.binding.nameTxt.text = facility.name
        var adapter = FacilitiesInnerAdapter(context, facility.media)
        var linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.binding.facilitiesInnerRv.adapter = adapter
        holder.binding.facilitiesInnerRv.layoutManager = linearLayoutManager
        adapter.setupListener(this)
    }

    override fun getItemCount(): Int {
        return facilities.size
    }

    override fun onItemClick(url: String, type: String) {
        listener.onItemClick(url, type)
    }


    class HospitalFecilityViewHolder(var binding: FacilityItemBinding) : ViewHolder(binding.root)

}