package com.example.allaboutfood_pricetalkdonate.PriceFragments

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.allaboutfood_pricetalkdonate.SharedPreferencesManager
import com.example.allaboutfood_pricetalkdonate.databinding.FragmentBigBasketBinding


class BigBasketFragment : Fragment() {

    private lateinit var binding : FragmentBigBasketBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBigBasketBinding.inflate(layoutInflater)
        SharedPreferencesManager.init(requireContext())


        val sharedPref = SharedPreferencesManager.getSharedPreferences()
        val item = sharedPref.getString("item","maggi")
        binding.webViewBigBasket.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.progressBarBigBasket.visibility = View.VISIBLE
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                binding.progressBarBigBasket.visibility = View.INVISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.webViewBigBasket.visibility = View.VISIBLE
            }
        }
        binding.webViewBigBasket.settings.domStorageEnabled = true;
        binding.webViewBigBasket.loadUrl("https://www.bigbasket.com/ps/?q=$item")
        //https://www.bigbasket.com/ps/?q=surf
        binding.webViewBigBasket.settings.useWideViewPort = false
        binding.webViewBigBasket.settings.javaScriptEnabled = true
        binding.webViewBigBasket.settings.setSupportZoom(true)
        binding.webViewBigBasket.settings.userAgentString = "Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3"
        // webView.settings.loadWithOverviewMode = true

        return binding.root
    }


}