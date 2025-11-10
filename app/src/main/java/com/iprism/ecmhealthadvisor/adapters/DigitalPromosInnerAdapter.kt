package com.iprism.ecmcorporatemarketing.adapters

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.FacilityItemBinding

import com.iprism.ecmhealthadvisor.databinding.InsuranceItemBinding
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.Media
import com.iprism.ecmhealthadvisor.utils.Constants


class DigitalPromosInnerAdapter(var context: Context, var imagesList : List<Media>) : RecyclerView.Adapter<DigitalPromosInnerAdapter.DigitalPromoInnerViewHodler>() {
      private lateinit var listener : OnFacilityInnerClickListener

    fun setupListener(listener: OnFacilityInnerClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DigitalPromoInnerViewHodler {
        var binding = InsuranceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DigitalPromoInnerViewHodler(binding)
    }

    override fun onBindViewHolder(
        holder: DigitalPromoInnerViewHodler,
        position: Int
    ) {
        holder.binding.plusImg.visibility = View.VISIBLE
        var media = imagesList[position]
        if (media.type == "image") {
            holder.binding.playBtn.visibility = View.GONE
            Glide.with(holder.itemView.context)
                .load(Constants.IMAGES_URL + media.url)
                .placeholder(R.drawable.logo)
                .into(holder.binding.facilityImg)

        } else if (media.type == "video") {

            holder.binding.playBtn.visibility = View.VISIBLE

            val retriever = MediaMetadataRetriever()
            try {
                retriever.setDataSource(Constants.IMAGES_URL + media.url, HashMap())
                val bitmap: Bitmap? = retriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST)
                if (bitmap != null) {
                    holder.binding.facilityImg.setImageBitmap(bitmap)
                } else {
                    holder.binding.facilityImg.setImageResource(R.drawable.logo)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                holder.binding.facilityImg.setImageResource(R.drawable.logo)
            } finally {
                retriever.release()
            }
        }

        holder.binding.plusImg.setOnClickListener { view ->
            listener.onItemClick(media.url, media.type)
        }
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

    interface OnFacilityInnerClickListener{

        fun onItemClick(url : String, type : String)

    }

    class DigitalPromoInnerViewHodler(var binding: InsuranceItemBinding) : RecyclerView.ViewHolder(binding.root)

}