package com.iprism.ecmhealthadvisor.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.ecmhealthadvisor.databinding.ActivityMainBinding
import com.iprism.ecmhealthadvisor.databinding.DigitalBrandingItemBinding

class AllDigitalBrandingsAdapter() :
    RecyclerView.Adapter<AllDigitalBrandingsAdapter.AllDigitalBrandingViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllDigitalBrandingsAdapter.AllDigitalBrandingViewHolder {
        var binding =
            DigitalBrandingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllDigitalBrandingViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AllDigitalBrandingsAdapter.AllDigitalBrandingViewHolder,
        position: Int
    ) {

    }

    override fun getItemCount(): Int {
        return 10
    }

    class AllDigitalBrandingViewHolder(var binding: DigitalBrandingItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}