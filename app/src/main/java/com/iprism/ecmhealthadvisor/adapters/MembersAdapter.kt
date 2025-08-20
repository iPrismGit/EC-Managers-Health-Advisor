package com.iprism.ecmhealthadvisor.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iprism.ecmhealthadvisor.databinding.ActivityMainBinding
import com.iprism.ecmhealthadvisor.databinding.PersonItemBinding

class MembersAdapter() : RecyclerView.Adapter<MembersAdapter.MemberViewHolder>() {
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
    }

    override fun getItemCount(): Int {
        return 4
    }

    class MemberViewHolder(var binding: PersonItemBinding) : RecyclerView.ViewHolder(binding.root)

}