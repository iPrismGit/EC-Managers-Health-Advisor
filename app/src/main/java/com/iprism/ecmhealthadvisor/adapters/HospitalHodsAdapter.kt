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

class HospitalHodsAdapter(var context: Context) :
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
        holder.binding.clinicNameTxt.visibility = RecyclerView.GONE
        holder.binding.nameTxt.text = "Dr. K. Subramanyam"
        holder.binding.clinicNameTxt.text = "Marketing"
        holder.binding.qualificationTxt.text = "Medical Superintendent"
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
        return 10
    }

    class HsopitalHodViewHolder(var binding: PersonItemBinding) : ViewHolder(binding.root)

}