package com.iprism.ecmhealthadvisor.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.iprism.ecmhealthadvisor.fragments.AddUsersFragment
import com.iprism.ecmhealthadvisor.fragments.HomeFragment
import com.iprism.ecmhealthadvisor.fragments.ProfileFragment

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> AddUsersFragment()
            2 -> ProfileFragment()
            else -> HomeFragment()
        }
    }

}