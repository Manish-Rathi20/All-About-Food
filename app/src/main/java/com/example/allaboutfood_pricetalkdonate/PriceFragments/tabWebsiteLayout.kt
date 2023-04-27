package com.example.allaboutfood_pricetalkdonate.PriceFragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.viewpager2.widget.ViewPager2
import com.example.allaboutfood_pricetalkdonate.Adapter.FragmentPageWebScrapeAdapter
import com.example.allaboutfood_pricetalkdonate.SharedPreferencesManager
import com.example.allaboutfood_pricetalkdonate.databinding.ActivityAmazonLayoutBinding
import com.google.android.material.tabs.TabLayout

class tabWebsiteLayout : AppCompatActivity() {
    private lateinit var binding : ActivityAmazonLayoutBinding
    private lateinit var adapter : FragmentPageWebScrapeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAmazonLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        SharedPreferencesManager.init(this)

        val sharedPref = SharedPreferencesManager.getSharedPreferences()
        val index = sharedPref.getInt("index",0)

        adapter = FragmentPageWebScrapeAdapter(supportFragmentManager,lifecycle)
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("FlipKart"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Amazon"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("BigBasket"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("JioMart") )
        binding.ViewPager2.adapter = adapter

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.ViewPager2.currentItem = index
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.ViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(index))
            }
        })



    }
}