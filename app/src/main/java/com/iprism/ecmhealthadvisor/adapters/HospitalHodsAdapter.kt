package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide

import com.iprism.ecmhealthadvisor.interfaces.OnSingleItemClickListener
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.PersonItemBinding
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.Doctor
import com.iprism.ecmhealthadvisor.utils.Constants

class HospitalHodsAdapter(var context: Context, var hods: List<Doctor>) :
    Adapter<HospitalHodsAdapter.HsopitalHodViewHolder>() {

    private lateinit var listener: OnSingleItemClickListener
    val bounce = AnimationUtils.loadAnimation(context, R.anim.bounce)

    fun setupListener(listener: OnSingleItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HospitalHodsAdapter.HsopitalHodViewHolder {
        var binding = PersonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HsopitalHodViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HospitalHodsAdapter.HsopitalHodViewHolder,
        position: Int
    ) {
        var hod = hods[position]
        holder.binding.clinicNameTxt.visibility = View.VISIBLE
        holder.binding.profileIv.borderColor = ContextCompat.getColor(context, R.color.green)
        holder.binding.profileIv.borderWidth = 4
        holder.binding.nameTxt.text = hod.name
        holder.binding.qualificationTxt.text = hod.qualification
        holder.binding.clinicNameTxt.text = hod.specialization
        if (hod.image.isNotEmpty()) {
            Glide.with(context).load(Constants.IMAGES_URL + hod.image).error(
                ContextCompat.getDrawable(context, R.drawable.customer_image)
            ).into(holder.binding.profileIv)
        } else {
            holder.binding.profileIv.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.customer_image
                )
            )
        }
        holder.binding.callNowLo.setOnClickListener(View.OnClickListener {
            it.startAnimation(bounce)
            listener.onCallNowClick(hod.id, hod.mobile)
        })
        holder.binding.smsLo.setOnClickListener(View.OnClickListener {
            it.startAnimation(bounce)
            listener.onSmsClick(hod.id, hod.mobile)
        })
        holder.binding.whatsAppLo.setOnClickListener(View.OnClickListener {
            it.startAnimation(bounce)
            listener.onWhatsappClick(hod.id, hod.mobile)
        })
    }

    override fun getItemCount(): Int {
        return hods.size
    }

    class HsopitalHodViewHolder(var binding: PersonItemBinding) : ViewHolder(binding.root)

}