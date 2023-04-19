package com.example.allaboutfood_pricetalkdonate.Fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.allaboutfood_pricetalkdonate.*
import com.example.allaboutfood_pricetalkdonate.PriceFragments.amazonLayout
import com.example.allaboutfood_pricetalkdonate.databinding.FragmentPriceBinding


class PriceFragment : Fragment() {

    private lateinit var binding: FragmentPriceBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPriceBinding.inflate(layoutInflater)
        SharedPreferencesManager.init(requireContext())
        val networkTalk = this.activity?.let { network(it.applicationContext) }
        val imageList = ArrayList<SlideModel>() // Create image list


        imageList.add(SlideModel("https://assets.entrepreneur.com/content/3x2/2000/20180511063849-flipkart-logo-detail-icon.jpeg"))
        imageList.add(SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT8m0VkfoZzfMFdBSFLvEO-_nYhp_gmKyHeHjmrVjY95ZKTABAo8c_m0ayb9Ea2cY3-rnw&usqp=CAU"))
        imageList.add(SlideModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSxzyUMirJLtz0iyMnIhyKMz5zMh4YRk5bAbw&usqp=CAU"))
        imageList.add(SlideModel("https://investorguruji.com/wp-content/uploads/2022/11/thumb-816x460-32a776eac605956b2d2ec52f5b4ff944.png"))
        binding.imageSlider.setImageList(imageList)
        binding.imageSlider.setItemClickListener(object : ItemClickListener {
            override fun onItemSelected(position: Int) {
                when (position) {
                    0 -> {
                        openWebPage("https://www.flipkart.com/")
                    }
                    1 -> {
                        openWebPage(
                            "http://www.amazon.com/gp\n" +
                                    "/mas/dl/android?"
                        )
                    }
                    2 -> {
                        openWebPage("https://www.bigbasket.com/")
                    }
                    3 -> {
                        openWebPage("https://www.jiomart.com/")
                    }
                }
            }
        })

        val sharedPref = SharedPreferencesManager.getSharedPreferences()
        val editor = sharedPref.edit()

        if (networkTalk?.isNetworkRequest() == true) {
            binding.btnGetItem.setOnClickListener {
                if (binding.etItem.text?.isNotBlank() == true) {
                    binding.filpcartLayout.visibility = VISIBLE
                    binding.bigBasketLayout.visibility = VISIBLE
                    binding.amazonLayout.visibility = VISIBLE
                    binding.jioMartLayout.visibility = VISIBLE
                    val itemName = binding.etItem.text.toString()
                    editor.apply {
                        putString("item", itemName)
                        apply()
                    }
                } else {
                    Toast.makeText(activity, "Can't be Empty", Toast.LENGTH_LONG).show()
                }
            }
            binding.filpcartLayout.setOnClickListener {
                var intent = Intent(activity, amazonLayout::class.java)
                intent.putExtra("flipkart", binding.etItem.text.toString())
                startActivity(intent)
            }
            binding.amazonLayout.setOnClickListener {
                var intent = Intent(activity, amazonLayout::class.java)
                intent.putExtra("amazon", binding.etItem.text.toString())
                startActivity(intent)
            }
            binding.bigBasketLayout.setOnClickListener {
                var intent = Intent(activity, amazonLayout::class.java)
                intent.putExtra(
                    "itemLink",
                    "https://www.bigbasket.com/ps/?q=${binding.etItem.text.toString()}"
                )
                startActivity(intent)
            }
            binding.jioMartLayout.setOnClickListener {
                var intent = Intent(activity, amazonLayout::class.java)
                intent.putExtra(
                    "itemLink",
                    "https://www.jiomart.com/search/${binding.etItem.text.toString()}/in/prod_mart_master_vertical"
                )
                startActivity(intent)
            }
        } else {
            Toast.makeText(activity, "Turn on Internet", Toast.LENGTH_LONG).show()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun openWebPage(url: String?) = url?.let {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
        startActivity(intent)
    }


}