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
import com.iprism.ecmhealthadvisor.interfaces.OnWhiteBoardClickListener
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.TariffCategory
import com.iprism.ecmhealthadvisor.utils.Constants

class HospitalTariffsAdapter(var context: Context, var tariffCategories: List<TariffCategory>) :
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
        var category = tariffCategories[position]
        holder.binding.nameTxt.text = category.name
        if (category.image.isNotEmpty()) {
            Glide.with(context).load(Constants.IMAGES_URL + category.image)
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
        holder.binding.root.setOnClickListener(View.OnClickListener {
            listener.onItemClick(category.id, category.name)
        })
    }

    override fun getItemCount(): Int {
        return tariffCategories.size
    }

    class HospitalTariffViewHolder(var binding: WhiteBoardFeedBackItemBinding) :
        ViewHolder(binding.root)

}