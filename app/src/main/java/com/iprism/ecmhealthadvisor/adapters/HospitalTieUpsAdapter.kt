package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.ecmhealthadvisor.databinding.FacilityItemBinding
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.Tieup

class HospitalTieUpsAdapter(var context: Context, var tieups: List<Tieup>) :
    Adapter<HospitalTieUpsAdapter.HospitalTieUpViewHolder>(),
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
    ): HospitalTieUpsAdapter.HospitalTieUpViewHolder {
        var binding =
            FacilityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HospitalTieUpViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HospitalTieUpsAdapter.HospitalTieUpViewHolder,
        position: Int
    ) {
        var tieup = tieups[position]
        holder.binding.nameTxt.text = tieup.name
        var adapter = FacilitiesInnerAdapter(context, tieup.media)
        var linearLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.binding.facilitiesInnerRv.adapter = adapter
        holder.binding.facilitiesInnerRv.layoutManager = linearLayoutManager
        adapter.setupListener(this)
    }

    override fun getItemCount(): Int {
        return tieups.size
    }

    override fun onItemClick(url: String, type: String) {
        listener.onItemClick(url, type)
    }

    class HospitalTieUpViewHolder(var binding: FacilityItemBinding) : ViewHolder(binding.root)

}