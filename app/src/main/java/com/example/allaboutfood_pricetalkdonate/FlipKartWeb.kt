package com.example.allaboutfood_pricetalkdonate

import android.content.ActivityNotFoundException
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.allaboutfood_pricetalkdonate.databinding.ActivityFlipKartWebBinding

class FlipKartWeb : AppCompatActivity() {
    private lateinit var binding : ActivityFlipKartWebBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFlipKartWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val item = intent.getStringExtra("itemLink")
        binding.FlipWeb.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.progressBarFlipWeb.visibility = View.VISIBLE
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                binding.progressBarFlipWeb.visibility = View.INVISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.FlipWeb.visibility = View.VISIBLE
            }
        }
        binding.FlipWeb.settings.domStorageEnabled = true;
        binding.FlipWeb.loadUrl("$item")
        //https://www.bigbasket.com/ps/?q=surf
        binding.FlipWeb.settings.useWideViewPort = false
        binding.FlipWeb.settings.javaScriptEnabled = true
        binding.FlipWeb.settings.setSupportZoom(true)
        binding.FlipWeb.settings.userAgentString = "Mozilla/5.0 (iPhone; U; CPU like Mac OS X; en) AppleWebKit/420+ (KHTML, like Gecko) Version/3.0 Mobile/1A543a Safari/419.3"
        // webView.settings.loadWithOverviewMode = true
    }
}