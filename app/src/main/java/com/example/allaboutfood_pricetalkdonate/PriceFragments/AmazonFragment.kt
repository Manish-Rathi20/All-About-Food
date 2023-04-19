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
import com.example.allaboutfood_pricetalkdonate.databinding.FragmentAmazonBinding


class AmazonFragment : Fragment() {
    private lateinit var binding : FragmentAmazonBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAmazonBinding.inflate(layoutInflater)
        SharedPreferencesManager.init(requireContext())


        val sharedPref = SharedPreferencesManager.getSharedPreferences()
        val item = sharedPref.getString("item","maggi")

       // val item = intent.getStringExtra("amazon")
        binding.webViewAmazon.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.progressBarAmazon.visibility = View.VISIBLE
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                binding.progressBarAmazon.visibility = View.INVISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.webViewAmazon.visibility = View.VISIBLE
            }
        }
        binding.webViewAmazon.settings.domStorageEnabled = true;
        binding.webViewAmazon.loadUrl("https://www.amazon.in/s?k=$item")
        //https://www.bigbasket.com/ps/?q=surf
        binding.webViewAmazon.settings.useWideViewPort = false
        binding.webViewAmazon.settings.javaScriptEnabled = true
        binding.webViewAmazon.settings.setSupportZoom(true)
        binding.webViewAmazon.settings.userAgentString = "Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3"
        // webView.settings.loadWithOverviewMode = true


        return binding.root
    }


}