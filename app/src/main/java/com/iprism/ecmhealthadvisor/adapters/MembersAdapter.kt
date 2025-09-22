package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.iprism.ecmhealthadvisor.interfaces.OnSingleItemClickListener
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.PersonItemBinding

class MembersAdapter(var context: Context) :
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
        holder.binding.nameTxt.text = "P Suresh Reddy"
        holder.binding.qualificationTxt.text = "Vice President"
        holder.binding.clinicNameTxt.visibility = View.VISIBLE
        holder.binding.clinicNameTxt.text = "Marketing"
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

    class MemberViewHolder(var binding: PersonItemBinding) : RecyclerView.ViewHolder(binding.root)

}