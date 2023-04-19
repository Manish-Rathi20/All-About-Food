package com.example.allaboutfood_pricetalkdonate.Fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.allaboutfood_pricetalkdonate.Adapter.FragmentPageAdapter
import com.example.allaboutfood_pricetalkdonate.databinding.FragmentDonateBinding
import com.google.android.material.tabs.TabLayout
import java.util.prefs.AbstractPreferences


open class DonateFragment : Fragment() {
    private lateinit var binding: FragmentDonateBinding
    private lateinit var adapter : FragmentPageAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDonateBinding.inflate(layoutInflater)
        adapter = FragmentPageAdapter(childFragmentManager,lifecycle)
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Organization"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Needy People"))
        binding.ViewPager2.adapter = adapter
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.ViewPager2.currentItem = tab?.position!!
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        binding.ViewPager2.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }


}
