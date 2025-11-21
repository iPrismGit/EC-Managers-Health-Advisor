package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.InsuranceItemBinding
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.Media
import com.iprism.ecmhealthadvisor.utils.Constants


class FacilitiesInnerAdapter(var context: Context, var imagesList: List<Media>) :
    RecyclerView.Adapter<FacilitiesInnerAdapter.FacilitiInnerViewHolder>() {

    private lateinit var listener: OnFacilityInnerClickListener

    fun setupListener(listener: OnFacilityInnerClickListener) {
        this.listener = listener
    }

    interface OnFacilityInnerClickListener {

        fun onItemClick(url: String, type: String)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FacilitiInnerViewHolder {
        var binding =
            InsuranceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FacilitiInnerViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: FacilitiInnerViewHolder,
        position: Int
    ) {
        var media = imagesList[position]
        if (media.type == "image") {
            holder.binding.playBtn.visibility = View.GONE
            Glide.with(holder.itemView.context)
                .load(Constants.IMAGES_URL + media.url)
                .placeholder(R.drawable.logo)
                .into(holder.binding.facilityImg)

        } else if (media.type == "video") {
            holder.binding.playBtn.visibility = View.VISIBLE
            holder.binding.facilityImg.setImageResource(R.drawable.logo)
        }

        holder.binding.root.setOnClickListener { view ->
            listener.onItemClick(media.url, media.type)
        }
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

    class FacilitiInnerViewHolder(var binding: InsuranceItemBinding) :
        RecyclerView.ViewHolder(binding.root)


}