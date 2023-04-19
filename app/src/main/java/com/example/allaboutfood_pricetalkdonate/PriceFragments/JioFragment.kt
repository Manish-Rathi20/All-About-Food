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
import com.example.allaboutfood_pricetalkdonate.databinding.FragmentJioBinding


class JioFragment : Fragment() {

    private lateinit var binding : FragmentJioBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJioBinding.inflate(layoutInflater)
        SharedPreferencesManager.init(requireContext())


        val sharedPref = SharedPreferencesManager.getSharedPreferences()
        val item = sharedPref.getString("item","maggi")
        binding.webViewJio.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.progressBarJio.visibility = View.VISIBLE
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                binding.progressBarJio.visibility = View.INVISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.webViewJio.visibility = View.VISIBLE
            }
        }
        binding.webViewJio.settings.domStorageEnabled = true;
        binding.webViewJio.loadUrl("https://www.jiomart.com/search/$item/in/prod_mart_master_vertical")
        //https://www.bigbasket.com/ps/?q=surf
        binding.webViewJio.settings.useWideViewPort = false
        binding.webViewJio.settings.javaScriptEnabled = true
        binding.webViewJio.settings.setSupportZoom(true)
        binding.webViewJio.settings.userAgentString = "Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3"
        // webView.settings.loadWithOverviewMode = true

        return binding.root
    }


}