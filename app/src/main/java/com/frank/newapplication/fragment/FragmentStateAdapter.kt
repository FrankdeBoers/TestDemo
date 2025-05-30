package com.frank.newapplication.fragment

import PageFragment
import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(val fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private var pageTitles = emptyList<String>()

    override fun getItemCount(): Int = pageTitles.size

    override fun containsItem(itemId: Long): Boolean {
        Log.i("FrankTest", "ViewPagerAdapter# containsItem itemId:$itemId pageTitles:$pageTitles")
        // item唯一性校验，要groupType + tagId
        return pageTitles.find {
            it.hashCode().toLong() == itemId
        } != null
    }

    override fun getItemId(position: Int): Long {
        // item唯一性校验，要groupType + tagId
        val data = pageTitles[position]
        Log.i("FrankTest", "ViewPagerAdapter# getItemId data:${data}")
        return data.hashCode().toLong()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun createFragment(position: Int): Fragment {
        Log.i("FrankTest", "ViewPagerAdapter# createFragment ${pageTitles[position]}")
        val result = PageFragment.newInstance(pageTitles[position]).apply {
            text = pageTitles[position]
        }
        fragmentActivity.fragmentManager.fragments.forEach { f ->
            Log.i("FrankTest", "ViewPagerAdapter# createFragment self:${f.hashCode()} activity:${(f.context as? Activity)?.hashCode()}")
        }
        return result

    }

    fun refreshData(list: List<String>) {
        pageTitles = list
        notifyDataSetChanged()
    }
}
