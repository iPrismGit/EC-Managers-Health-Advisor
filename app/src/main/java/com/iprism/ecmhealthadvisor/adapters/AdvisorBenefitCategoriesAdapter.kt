package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iprism.ecmhealthadvisor.R
import com.iprism.ecmhealthadvisor.databinding.ActivityAddMemberBinding
import com.iprism.ecmhealthadvisor.databinding.WhiteBoardFeedBackItemBinding
import com.iprism.ecmhealthadvisor.interfaces.OnBenefitClickListener
import com.iprism.ecmhealthadvisor.modals.healthadvisorbenefits.BenefitsResponse
import com.iprism.ecmhealthadvisor.utils.Constants

class AdvisorBenefitCategoriesAdapter(var context: Context, var categories: List<BenefitsResponse>) : RecyclerView.Adapter<AdvisorBenefitCategoriesAdapter.AdvisorBenefitCategoryViewHolder>() {

    private lateinit var listener: OnBenefitClickListener

    fun setupListener(listener: OnBenefitClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdvisorBenefitCategoriesAdapter.AdvisorBenefitCategoryViewHolder {
        var binding = WhiteBoardFeedBackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdvisorBenefitCategoryViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: AdvisorBenefitCategoriesAdapter.AdvisorBenefitCategoryViewHolder,
        position: Int
    ) {
        var category = categories[position]
        holder.binding.nameTxt.text = category.name
        if (category.image.isNotEmpty()){
            Glide.with(context).load(Constants.IMAGES_URL + category.image)
                .error(ContextCompat.getDrawable(context, R.drawable.logo))
                .into(holder.binding.categoryImg)
        } else {
            holder.binding.categoryImg.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.logo))
        }

        holder.binding.root.setOnClickListener { view ->
            listener.onItemClick(category.id, category.name)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    class AdvisorBenefitCategoryViewHolder(var binding: WhiteBoardFeedBackItemBinding) : RecyclerView.ViewHolder(binding.root)

}