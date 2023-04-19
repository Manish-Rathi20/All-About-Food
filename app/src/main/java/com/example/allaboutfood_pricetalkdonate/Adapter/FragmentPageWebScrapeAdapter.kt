package com.example.allaboutfood_pricetalkdonate.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.allaboutfood_pricetalkdonate.PriceFragments.AmazonFragment
import com.example.allaboutfood_pricetalkdonate.PriceFragments.BigBasketFragment
import com.example.allaboutfood_pricetalkdonate.PriceFragments.FlipKartFragment
import com.example.allaboutfood_pricetalkdonate.PriceFragments.JioFragment


class FragmentPageWebScrapeAdapter(
    fragmentManager: FragmentManager,
    lifecycle:Lifecycle
):FragmentStateAdapter(fragmentManager,lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
     return when(position){
         0-> FlipKartFragment()
         1-> AmazonFragment()
         2-> BigBasketFragment()
         else -> JioFragment()
     }
    }
}