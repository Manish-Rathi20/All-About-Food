package com.example.allaboutfood_pricetalkdonate.PriceFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allaboutfood_pricetalkdonate.Adapter.ItemDataAdapter
import com.example.allaboutfood_pricetalkdonate.Model.ItemData
import com.example.allaboutfood_pricetalkdonate.SharedPreferencesManager
import com.example.allaboutfood_pricetalkdonate.databinding.FragmentFlipKartBinding
import com.example.allaboutfood_pricetalkdonate.`interface`.getItemServices
import retrofit2.Call
import retrofit2.Response


class FlipKartFragment : Fragment() {
    private lateinit var binding : FragmentFlipKartBinding
    private lateinit var adapter : ItemDataAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFlipKartBinding.inflate(layoutInflater)
        SharedPreferencesManager.init(requireContext())


        val sharedPref = SharedPreferencesManager.getSharedPreferences()
        val item = sharedPref.getString("item","maggi")
        if(item != null){
            getData(item)
        }

        return binding.root
    }

    private fun getData(text: String) {
        val getItems = getItemServices.items.getItem(text)
        getItems.enqueue(object : retrofit2.Callback<ItemData> {
            override fun onResponse(call: Call<ItemData>, response: Response<ItemData>) {
                val itemResponse = response.body()
                if(itemResponse != null){
                    Log.d("FlipKart", itemResponse.result.toString() )
                    adapter = ItemDataAdapter(itemResponse.result)
                    binding.rvProductFlipkart.adapter = adapter
                    binding.rvProductFlipkart.layoutManager = LinearLayoutManager(requireContext())

                }else{
                    Toast.makeText(activity,"Not get item", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ItemData>, t: Throwable) {
                Toast.makeText(activity,"${t.message}", Toast.LENGTH_LONG).show()
                Log.d("FlipKart", t.message.toString())

            }
        })
    }


}