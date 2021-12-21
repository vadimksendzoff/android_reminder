package com.qrolic.reminderapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.qrolic.reminderapp.R
import com.qrolic.reminderapp.activity.AddNewReminder
import com.qrolic.reminderapp.activity.SearchActivity
import com.qrolic.reminderapp.adapter.ViewPagerAdapter
import com.qrolic.reminderapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        initAll()
        return fragmentHomeBinding.root
    }

    private fun initAll() {
      setHasOptionsMenu(true)
        fragmentHomeBinding.fabAddReminder.setOnClickListener(View.OnClickListener {

            startAddRemiderActivity()
        })
        tabLayout = fragmentHomeBinding.tlMain
        viewPager = fragmentHomeBinding.vpMain

        tabLayout!!.addTab(tabLayout!!.newTab().setText("All"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("Favorites"))
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL

        val adapter = ViewPagerAdapter(requireContext(), activity?.supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = adapter

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                viewPager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
                //onTabUnselected() method
            }
            override fun onTabReselected(tab: TabLayout.Tab) {
                //onTabReselected() method
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_search, menu)
        val menuItem = menu.findItem(R.id.home_search)
       menuItem.setOnMenuItemClickListener { view->
           val intent = Intent(context,SearchActivity::class.java)
           intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
           activity?.startActivity(intent)
           return@setOnMenuItemClickListener true
       }

        super.onCreateOptionsMenu(menu, inflater)
    }

 fun startAddRemiderActivity()
    {
        val intent=Intent(context?.applicationContext,AddNewReminder::class.java);
        startActivity(intent)
    }
}