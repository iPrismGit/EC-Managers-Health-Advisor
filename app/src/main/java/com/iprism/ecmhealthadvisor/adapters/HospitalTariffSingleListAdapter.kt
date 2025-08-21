package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.ecmhealthadvisor.databinding.WhiteBoardFeedBackItemBinding

class HospitalTariffSingleListAdapter(var context: Context) : Adapter<HospitalTariffSingleListAdapter.HospitalTariffSingleItemViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HospitalTariffSingleItemViewHolder {
        var binding = WhiteBoardFeedBackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HospitalTariffSingleItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HospitalTariffSingleItemViewHolder,
        position: Int
    ) {
        holder.binding.nameTxt.text = "Gynecologist"
        holder.binding.priceTxt.visibility = View.VISIBLE

    }

    override fun getItemCount(): Int {
        return 20
    }

    class HospitalTariffSingleItemViewHolder(var binding: WhiteBoardFeedBackItemBinding) : ViewHolder(binding.root)

}