package com.yuan.testjetpack.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.yuan.testjetpack.R
import com.yuan.testjetpack.fragment.FirstFragment
import com.yuan.testjetpack.fragment.SecondFragment
import com.yuan.testjetpack.fragment.ThirdFragment
import kotlinx.android.synthetic.main.activity_view_pager.*

class ViewPagerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager)
        viewpager2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int {
                return 3
            }

            override fun createFragment(position: Int) =
                    when (position) {
                        0 -> FirstFragment()
                        1 -> SecondFragment()
                        else -> ThirdFragment()
                    }
        }

        TabLayoutMediator(tablayout, viewpager2) {tab, position ->
            when(position) {
                0 -> tab.text = "缩放"
                1 -> tab.text = "旋转"
                else -> tab.text = "移动"
            }
        }.attach()

    }
}