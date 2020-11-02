package com.setiawan.mysubmission.ui.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.setiawan.mysubmission.ui.fragment.FollowersFragment
import com.setiawan.mysubmission.ui.fragment.FollowingFragment
import com.setiawan.mysubmission.R

class ViewPagerDetailAdapter(private val mContext : Context , fm : FragmentManager ) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val pages = listOf(
        FollowersFragment(),
        FollowingFragment()
    )
    private val tab_title = intArrayOf(
        R.string.follwer,
        R.string.follwing
    )
    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(tab_title[position])
    }
}