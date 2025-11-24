package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.WhiteBoardFeedBackItemBinding
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.Tariff
import com.iprism.ecmhealthadvisor.utils.Constants
import kotlin.toString

class HospitalTariffSingleListAdapter(var context: Context, var tariffs: List<Tariff>) :
    Adapter<HospitalTariffSingleListAdapter.HospitalTariffSingleItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HospitalTariffSingleItemViewHolder {
        var binding = WhiteBoardFeedBackItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HospitalTariffSingleItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HospitalTariffSingleItemViewHolder,
        position: Int
    ) {
        var tariff = tariffs[position]
        holder.binding.nameTxt.text = tariff.name
        holder.binding.priceTxt.text =
            "₹" + tariff.from_price.toString() + " to ₹" + tariff.to_price
        holder.binding.priceTxt.visibility = View.VISIBLE
        if (tariff.image.isNotEmpty()) {
            Glide.with(context).load(Constants.IMAGES_URL + tariff.image)
                .error(ContextCompat.getDrawable(context, R.drawable.logo))
                .into(holder.binding.categoryImg)
        } else {
            holder.binding.categoryImg.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.logo
                )
            )
        }

    }

    override fun getItemCount(): Int {
        return tariffs.size
    }

    class HospitalTariffSingleItemViewHolder(var binding: WhiteBoardFeedBackItemBinding) :
        ViewHolder(binding.root)

}