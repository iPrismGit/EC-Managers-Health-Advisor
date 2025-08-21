package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.ecmhealthadvisor.databinding.WhiteBoardFeedBackItemBinding
import com.iprism.ecmhealthadvisor.interfaces.OnWhiteBoardClickListener

class HospitalTariffsAdapter(var context: Context) :
    Adapter<HospitalTariffsAdapter.HospitalTariffViewHolder>() {

    private lateinit var listener: OnWhiteBoardClickListener

    fun setupListener(listener: OnWhiteBoardClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HospitalTariffViewHolder {
        var binding = WhiteBoardFeedBackItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HospitalTariffViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HospitalTariffViewHolder,
        position: Int
    ) {
        holder.binding.nameTxt.text = "OP Consultations"
        holder.binding.root.setOnClickListener(View.OnClickListener {
            listener.onItemClick("")
        })
    }

    override fun getItemCount(): Int {
        return 12
    }

    class HospitalTariffViewHolder(var binding: WhiteBoardFeedBackItemBinding) :
        ViewHolder(binding.root)
}