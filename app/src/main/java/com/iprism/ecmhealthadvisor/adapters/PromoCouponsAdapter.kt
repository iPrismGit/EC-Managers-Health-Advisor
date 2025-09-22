package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.InsuranceItemBinding

class PromoCouponsAdapter(var context: Context) :
    Adapter<PromoCouponsAdapter.PromoCouponViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PromoCouponsAdapter.PromoCouponViewHolder {
        var binding =
            InsuranceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PromoCouponViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PromoCouponsAdapter.PromoCouponViewHolder,
        position: Int
    ) {
        holder.binding.imageView6.setImageDrawable(
            ContextCompat.getDrawable(
                context,
                R.drawable.to_do_list_img
            )
        )
    }

    override fun getItemCount(): Int {
        return 6
    }

    class PromoCouponViewHolder(var binding: InsuranceItemBinding) : ViewHolder(binding.root)

}