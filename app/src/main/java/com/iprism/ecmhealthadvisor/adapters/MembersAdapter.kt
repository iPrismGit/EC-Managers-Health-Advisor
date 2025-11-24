package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.ecmhealthadvisor.interfaces.OnSingleItemClickListener
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.PersonItemBinding
import com.iprism.ecmhealthadvisor.modals.addleads.Lead
import com.iprism.ecmhealthadvisor.utils.Constants

class MembersAdapter(var context: Context, var leads: List<Lead>) :
    RecyclerView.Adapter<MembersAdapter.MemberViewHolder>() {

    private lateinit var listener: OnSingleItemClickListener
    val bounce = AnimationUtils.loadAnimation(context, R.anim.bounce)

    fun setupListener(listener: OnSingleItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MembersAdapter.MemberViewHolder {
        var binding = PersonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MembersAdapter.MemberViewHolder, position: Int) {
        var lead = leads[position]
        holder.binding.nameTxt.text = lead.name
        holder.binding.qualificationTxt.text = lead.profession
        holder.binding.clinicNameTxt.visibility = View.GONE
        holder.binding.profileIv.borderColor = ContextCompat.getColor(context, R.color.green)
        holder.binding.profileIv.borderWidth = 4
        if (lead.image.isNotEmpty()) {
            Glide.with(context).load(Constants.IMAGES_URL + lead.image).error(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.customer_image
                )
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
            listener.onCallNowClick(lead.id, lead.mobile)
            it.startAnimation(bounce)
        })

        holder.binding.smsLo.setOnClickListener(View.OnClickListener {
            listener.onSmsClick(lead.id, lead.mobile)
            it.startAnimation(bounce)
        })

        holder.binding.whatsAppLo.setOnClickListener(View.OnClickListener {
            listener.onWhatsappClick(lead.id, lead.mobile)
            it.startAnimation(bounce)
        })
    }

    override fun getItemCount(): Int {
        return leads.size
    }

    class MemberViewHolder(var binding: PersonItemBinding) : RecyclerView.ViewHolder(binding.root)

}