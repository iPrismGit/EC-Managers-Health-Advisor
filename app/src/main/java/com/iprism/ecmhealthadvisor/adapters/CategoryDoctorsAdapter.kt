package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.PersonItemBinding
import com.iprism.ecmhealthadvisor.interfaces.OnSingleItemClickListener
import com.iprism.ecmhealthadvisor.modals.hospitalmodels.Doctor
import com.iprism.ecmhealthadvisor.utils.Constants

class CategoryDoctorsAdapter(var context: Context, var doctors: List<Doctor>): Adapter<CategoryDoctorsAdapter.CategoryDoctorViewHolder> () {

    private lateinit var listener: OnSingleItemClickListener
    val bounce = AnimationUtils.loadAnimation(context, R.anim.bounce)

    fun setupListener(listener: OnSingleItemClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryDoctorsAdapter.CategoryDoctorViewHolder {
        var binding = PersonItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryDoctorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryDoctorsAdapter.CategoryDoctorViewHolder, position: Int) {
        var doctor = doctors[position]
        holder.binding.clinicNameTxt.visibility = View.VISIBLE
        holder.binding.profileIv.borderColor = ContextCompat.getColor(context, R.color.green)
        holder.binding.profileIv.borderWidth = 4
        holder.binding.nameTxt.text = doctor.name
        holder.binding.qualificationTxt.text = doctor.qualification
        holder.binding.clinicNameTxt.text = doctor.specialization
        if (doctor.image.isNotEmpty()){
            Glide.with(context).load(Constants.IMAGES_URL + doctor.image).error(
                ContextCompat.getDrawable(context, R.drawable.customer_image)).into(holder.binding.profileIv)
        } else{
            holder.binding.profileIv.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.customer_image))
        }
        holder.binding.callNowLo.setOnClickListener(View.OnClickListener {
            it.startAnimation(bounce)
            listener.onCallNowClick(doctor.id, doctor.mobile)
        })
        holder.binding.smsLo.setOnClickListener(View.OnClickListener {
            it.startAnimation(bounce)
            listener.onSmsClick(doctor.id, doctor.mobile)
        })
        holder.binding.whatsAppLo.setOnClickListener(View.OnClickListener {
            it.startAnimation(bounce)
            listener.onWhatsappClick(doctor.id, doctor.mobile)
        })
    }

    override fun getItemCount(): Int {
        return doctors.size
    }

    class CategoryDoctorViewHolder(var binding: PersonItemBinding) : ViewHolder(binding.root)

}