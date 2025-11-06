package com.iprism.ecmhealthadvisor.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.iprism.ecmhealthadvisor.databinding.BannerItemBinding
import com.iprism.ecmhealthadvisor.modals.homepagemodels.Banner
import com.iprism.ecmhealthadvisor.utils.Constants

import com.smarteist.autoimageslider.SliderViewAdapter

class BannersAdapter (private val banners: List<Banner>) :
    SliderViewAdapter<BannersAdapter.BannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?): BannersAdapter.BannerViewHolder {
        var binding = BannerItemBinding.inflate( LayoutInflater.from(parent!!.context), parent, false)
        return BannerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BannersAdapter.BannerViewHolder, p1: Int) {
        var banner = banners[p1]
        Glide.with(holder.binding.root.context)
            .load(Constants.IMAGES_URL + banner.image)
            .fitCenter()
            .into(holder.binding.bannerImg)
    }

    override fun getCount(): Int {
        return banners.size
    }

    class BannerViewHolder(var binding: BannerItemBinding) : ViewHolder(binding.root)

}