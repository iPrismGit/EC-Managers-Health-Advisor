package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.VISIBLE
import androidx.recyclerview.widget.RecyclerView.ViewHolder

import com.iprism.ecmhealthadvisor.interfaces.OnSingleItemClickListener
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.PersonItemBinding


class MarketingTeamsAdapter(var context: Context) :
    Adapter<MarketingTeamsAdapter.MarketingTeamViewHolder>() {

    private lateinit var listener: OnSingleItemClickListener
    val bounce = AnimationUtils.loadAnimation(context, R.anim.bounce)

    fun setupListener(listener: OnSingleItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MarketingTeamViewHolder {
        var binding = PersonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarketingTeamViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MarketingTeamViewHolder,
        position: Int
    ) {
        holder.binding.clinicNameTxt.visibility = VISIBLE
        holder.binding.nameTxt.text = "P Suresh Reddy"
        holder.binding.clinicNameTxt.text = "Marketing"
        holder.binding.qualificationTxt.text = "Vice President"
        holder.binding.callNowLo.setOnClickListener(View.OnClickListener {
            listener.onCallNowClick("", "")
            it.startAnimation(bounce)
        })

        holder.binding.smsLo.setOnClickListener(View.OnClickListener {
            listener.onSmsClick("", "")
            it.startAnimation(bounce)
        })

        holder.binding.whatsAppLo.setOnClickListener(View.OnClickListener {
            listener.onWhatsappClick("", "")
            it.startAnimation(bounce)
        })
    }

    override fun getItemCount(): Int {
        return 4
    }

    class MarketingTeamViewHolder(var binding: PersonItemBinding) : ViewHolder(binding.root)

}