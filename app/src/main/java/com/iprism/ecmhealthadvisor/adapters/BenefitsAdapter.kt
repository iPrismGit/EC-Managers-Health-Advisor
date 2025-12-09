package com.iprism.ecmhealthadvisor.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.ecmhealthadvisor.databinding.BenefitItemBinding
import com.iprism.ecmhealthadvisor.interfaces.OnBenefitClickListener
import com.iprism.ecmhealthadvisor.modals.healthadvisorbenefits.BenefitsResponse

class BenefitsAdapter(private val context: Context, private val benefits: List<BenefitsResponse> ) : RecyclerView.Adapter<BenefitsAdapter.BenefitViewHolder>() {

    private val selectedIds = mutableSetOf<String>()
    private lateinit var listener : OnBenefitClickListener

    fun setupListener(listener: OnBenefitClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BenefitViewHolder {
        val binding = BenefitItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BenefitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BenefitViewHolder, position: Int) {
        val benefit = benefits[position]

        holder.binding.apply {
            benefitNameCb.text = benefit.name
            benefitNameCb.isChecked = selectedIds.contains(benefit.id)

            benefitNameCb.setOnCheckedChangeListener { _, isChecked ->
                val id = benefit.id ?: ""

                if (isChecked) selectedIds.add(id)
                else selectedIds.remove(id)

                val camaSeparatedValue = selectedIds.joinToString(",")

                listener.onItemClick(camaSeparatedValue, "")
            }
        }
    }

    override fun getItemCount() = benefits.size

    class BenefitViewHolder(val binding: BenefitItemBinding) : RecyclerView.ViewHolder(binding.root)
}
