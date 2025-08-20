package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.iprism.ecmhealthadvisor.interfaces.OnSingleItemClickListener
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.PersonItemBinding


class InboundTeamsAdapter(var context: Context): Adapter<InboundTeamsAdapter.InboundTeamViewHolder>() {

    private lateinit var listener : OnSingleItemClickListener
    val bounce = AnimationUtils.loadAnimation(context, R.anim.bounce)

    fun setupListener(listener: OnSingleItemClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InboundTeamViewHolder {
        var binding = PersonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InboundTeamViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: InboundTeamViewHolder,
        position: Int
    ) {
        holder.binding.clinicNameTxt.visibility = RecyclerView.VISIBLE
        holder.binding.nameTxt.text = "N Samantha"
        holder.binding.clinicNameTxt.text = "Hospital Co-ordinator"
        holder.binding.qualificationTxt.text = "Front Office Manager"
        holder.binding.callNowLo.setOnClickListener(View.OnClickListener {
            listener.onCallNowClick("", "")
            it.startAnimation(bounce)
        })

        holder.binding.smsLo.setOnClickListener(View.OnClickListener {
            it.startAnimation(bounce)
            listener.onSmsClick("", "")
        })

        holder.binding.whatsAppLo.setOnClickListener(View.OnClickListener {
            it.startAnimation(bounce)
            listener.onWhatsappClick("", "")
        })
    }

    override fun getItemCount(): Int {
        return 5
    }
    class InboundTeamViewHolder(var binding: PersonItemBinding) : ViewHolder(binding.root)
}


