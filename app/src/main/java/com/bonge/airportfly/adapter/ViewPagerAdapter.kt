package com.bonge.airportfly.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    var list: List<Fragment> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = list.size

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}