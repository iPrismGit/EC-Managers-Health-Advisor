package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.ecmhealthadvisor.databinding.PromoCouponItemBinding

class SinglePromoCouponsAdapter(var context: Context) :
    Adapter<SinglePromoCouponsAdapter.SinglePromoCouponViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SinglePromoCouponsAdapter.SinglePromoCouponViewHolder {
        val binding =
            PromoCouponItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SinglePromoCouponViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: SinglePromoCouponsAdapter.SinglePromoCouponViewHolder,
        position: Int
    ) {

    }

    override fun getItemCount(): Int {
        return 10
    }

    class SinglePromoCouponViewHolder(var binding: PromoCouponItemBinding) :
        ViewHolder(binding.root)

}