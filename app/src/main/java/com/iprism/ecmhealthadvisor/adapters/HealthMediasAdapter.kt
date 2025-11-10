package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.HealthMediaItemBinding
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.MainData
import com.iprism.ecmhealthadvisor.utils.Constants

class HealthMediasAdapter(var context: Context, var images: List<MainData>) : RecyclerView.Adapter<HealthMediasAdapter.HealthMediaViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HealthMediasAdapter.HealthMediaViewHolder {
        var binding = HealthMediaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HealthMediaViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HealthMediasAdapter.HealthMediaViewHolder,
        position: Int
    ) {
        var media = images[position]
        holder.binding.dateTxt.text = media.created_on
        if (media.image.isNotEmpty()){
            Glide.with(context).load(Constants.IMAGES_URL + media.image).error(ContextCompat.getDrawable(context, R.drawable.logo)).into(holder.binding.mediaIv)
        } else{
            holder.binding.mediaIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo))
        }
    }

    override fun getItemCount(): Int {
        return images.size
    }

    class HealthMediaViewHolder(var binding: HealthMediaItemBinding) : RecyclerView.ViewHolder(binding.root)

}