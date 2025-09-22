package com.iprism.ecmhealthadvisor.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.ecmhealthadvisor.databinding.BenefitItemBinding

class BenefitsAdapter() : RecyclerView.Adapter<BenefitsAdapter.BenefitViewHOlder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BenefitsAdapter.BenefitViewHOlder {
        var binding = BenefitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BenefitViewHOlder(binding)
    }

    override fun onBindViewHolder(holder: BenefitsAdapter.BenefitViewHOlder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 10
    }

    class BenefitViewHOlder(var binding: BenefitItemBinding) : RecyclerView.ViewHolder(binding.root)

}