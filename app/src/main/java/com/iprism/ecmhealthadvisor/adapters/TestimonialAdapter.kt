package com.iprism.ecmhealthadvisor.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.iprism.ecmhealthadvisor.modals.hospitalmodels.Video

import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.VideoItemBinding
import com.iprism.ecmhealthadvisor.interfaces.TestimonialClickListener
import com.iprism.ecmhealthadvisor.utils.Constants


class TestimonialAdapter(var context: Context, var videos: List<Video>) :
    RecyclerView.Adapter<TestimonialAdapter.TestimonialViewHolder>() {

    private lateinit var listener: TestimonialClickListener

    fun setupListener(listener: TestimonialClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TestimonialAdapter.TestimonialViewHolder {
        var binding = VideoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TestimonialViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: TestimonialAdapter.TestimonialViewHolder,
        position: Int
    ) {
        var video = videos[position]
        holder.binding.videoLinkTxt.text = "Video Link : " + video.link
        if (video.image.isNotEmpty()) {
            Glide.with(context).load(Constants.IMAGES_URL + video.image)
                .error(ContextCompat.getDrawable(context, R.drawable.logo))
                .into(holder.binding.thumbnailIv)
        } else {
            holder.binding.thumbnailIv.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.logo
                )
            )
        }
        holder.binding.root.setOnClickListener { view ->
            listener.onVideoClick(video.link)
        }
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    class TestimonialViewHolder(var binding: VideoItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}