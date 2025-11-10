package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iprism.ecmcorporatemarketing.adapters.DigitalPromosInnerAdapter
import com.iprism.ecmhealthadvisor.databinding.FacilityItemBinding

import com.iprism.ecmhealthadvisor.modals.hospitalmodels.DigitalPromo

class DigitalPromosAdapter(var context: Context, var digitalPromos: List<DigitalPromo>) : RecyclerView.Adapter<DigitalPromosAdapter.DigitalPromoViewHolder> (),  DigitalPromosInnerAdapter.OnFacilityInnerClickListener  {

    private lateinit var  listener: OnFacilityOuterClickListener


    fun setupListener(listener: OnFacilityOuterClickListener){
        this.listener = listener
    }
    interface OnFacilityOuterClickListener {
        fun onItemClick(url: String, type: String)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DigitalPromoViewHolder {
        var binding = FacilityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DigitalPromoViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DigitalPromoViewHolder,
        position: Int
    ) {
        var promo = digitalPromos[position]
        holder.binding.nameTxt.text = promo.name
        var adapter = DigitalPromosInnerAdapter(context, promo.media)
        var linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.binding.facilitiesInnerRv.adapter = adapter
        holder.binding.facilitiesInnerRv.layoutManager = linearLayoutManager
        adapter.setupListener(this)
    }

    override fun getItemCount(): Int {
        return digitalPromos.size
    }

    override fun onItemClick(url: String, type: String) {
        listener.onItemClick(url, type)
    }

    class DigitalPromoViewHolder(var binding: FacilityItemBinding) : RecyclerView.ViewHolder(binding.root)
}