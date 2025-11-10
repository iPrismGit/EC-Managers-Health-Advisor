package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.VISIBLE
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide

import com.iprism.ecmhealthadvisor.interfaces.OnSingleItemClickListener
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.PersonItemBinding
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.PanelAdvisor
import com.iprism.ecmhealthadvisor.utils.Constants


class MarketingTeamsAdapter(var context: Context, var employees : List<PanelAdvisor> ) :
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
        var employee = employees[position]
        holder.binding.profileIv.borderColor = ContextCompat.getColor(context, R.color.green)
        holder.binding.profileIv.borderWidth = 4
        holder.binding.nameTxt.text = employee.name
        holder.binding.qualificationTxt.text = employee.designation
        holder.binding.clinicNameTxt.visibility = View.VISIBLE
        holder.binding.clinicNameTxt.text = "Marketing Team"
        if (employee.image.isNotEmpty()){
            Glide.with(context).load(Constants.IMAGES_URL + employee.image).error(
                ContextCompat.getDrawable(context, R.drawable.customer_image)).into(holder.binding.profileIv)
        } else{
            holder.binding.profileIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.customer_image))
        }
        holder.binding.callNowLo.setOnClickListener(View.OnClickListener {
            listener.onCallNowClick(employee.id, employee.mobile)
            it.startAnimation(bounce)
        })

        holder.binding.smsLo.setOnClickListener(View.OnClickListener {
            it.startAnimation(bounce)
            listener.onSmsClick(employee.id, employee.mobile)
        })

        holder.binding.whatsAppLo.setOnClickListener(View.OnClickListener {
            it.startAnimation(bounce)
            listener.onWhatsappClick(employee.id, employee.mobile)
        })
    }

    override fun getItemCount(): Int {
        return employees.size
    }

    class MarketingTeamViewHolder(var binding: PersonItemBinding) : ViewHolder(binding.root)

}