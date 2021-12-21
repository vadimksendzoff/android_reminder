package com.qrolic.reminderapp.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import com.qrolic.reminderapp.fragment.AllReminderFragment
import com.qrolic.reminderapp.fragment.FavouritesReminderFragment

@Suppress("DEPRECATION")
class ViewPagerAdapter(
    private val myContext: Context,
    fm: FragmentManager?,
    internal var totalTabs: Int
) : FragmentPagerAdapter(
    fm!!
) {

    // this is for fragment tabs
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                AllReminderFragment()
            }
            1 -> {
                FavouritesReminderFragment()
            }
            else -> getItem(position)
        }
    }

    // this counts total number of tabs
    override fun getCount(): Int {
        return totalTabs
    }
}