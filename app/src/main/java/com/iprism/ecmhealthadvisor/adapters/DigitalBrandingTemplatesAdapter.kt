package com.iprism.ecmhealthadvisor.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.ecmhealthadvisor.databinding.ActivityMainBinding
import com.iprism.ecmhealthadvisor.databinding.DigitalBrandingTemplateItemBinding
import com.iprism.ecmhealthadvisor.interfaces.OnSingleItemClickListener
import com.iprism.ecmhealthadvisor.interfaces.OnWhiteBoardClickListener

class DigitalBrandingTemplatesAdapter() : RecyclerView.Adapter<DigitalBrandingTemplatesAdapter.DigitalBrandingTemplateViewHolder>() {

    private lateinit var listener: OnWhiteBoardClickListener

    fun setupListener(listener: OnWhiteBoardClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DigitalBrandingTemplatesAdapter.DigitalBrandingTemplateViewHolder {
        var binding = DigitalBrandingTemplateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DigitalBrandingTemplateViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: DigitalBrandingTemplatesAdapter.DigitalBrandingTemplateViewHolder,
        position: Int
    ) {
        holder.binding.root.setOnClickListener { view ->
            listener.onItemClick("")
        }
    }

    override fun getItemCount(): Int {
       return 6
    }

    class DigitalBrandingTemplateViewHolder(var binding: DigitalBrandingTemplateItemBinding) : RecyclerView.ViewHolder(binding.root)
}