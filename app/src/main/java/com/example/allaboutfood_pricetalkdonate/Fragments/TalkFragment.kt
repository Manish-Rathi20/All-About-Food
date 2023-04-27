package com.example.allaboutfood_pricetalkdonate.Fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allaboutfood_pricetalkdonate.Post.CreatePostActivity
import com.example.allaboutfood_pricetalkdonate.Adapter.TalkAdapter
import com.example.allaboutfood_pricetalkdonate.Model.postss
import com.example.allaboutfood_pricetalkdonate.R
import com.example.allaboutfood_pricetalkdonate.databinding.FragmentTalkBinding
import com.example.allaboutfood_pricetalkdonate.network
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.*
import java.util.*

class TalkFragment : Fragment() {
    private lateinit var binding: FragmentTalkBinding
    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    lateinit var auth: FirebaseAuth
    private lateinit var adapter: TalkAdapter
    var postList = mutableListOf<postss>()
    var post = mutableListOf<postss>()
    private lateinit var fireStoreDb: FirebaseFirestore
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTalkBinding.inflate(layoutInflater)
        val root: View = binding.root
        val networkTalk = this.activity?.let { network(it.applicationContext) }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if(networkTalk?.isNetworkRequest() == true) {
            auth = FirebaseAuth.getInstance()
            fireStoreDb = FirebaseFirestore.getInstance()
            getCurrentLocation()
            adapter = TalkAdapter(container!!.context, post)
            binding.rvTalk.adapter = adapter
            binding.rvTalk.layoutManager = LinearLayoutManager(activity)
            binding.floatButtonPost.setOnClickListener {
                startActivity(Intent(activity, CreatePostActivity::class.java))
            }
        }else{
            Toast.makeText(activity, "Turn on Internet", Toast.LENGTH_LONG).show()
        }
        return root
    }

    private fun getPost(cityName: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                fireStoreDb.collection("posts")
                    .orderBy("creation_time",Query.Direction.DESCENDING)
                    .whereEqualTo("location",cityName)
                    .addSnapshotListener { snapshot, exception ->
                    if (exception != null || snapshot == null) {
                        Toast.makeText(activity, "somthing went wrong${exception?.message}", Toast.LENGTH_LONG).show()
                        Log.d("TalkFragment", "${exception?.message}")
                        return@addSnapshotListener
                    }
                    postList = snapshot.toObjects(postss::class.java)
                    post.clear()
                    post.addAll(postList)
                    adapter.notifyDataSetChanged()

                }

            } catch (e: Exception) {
                Log.d("TalkFragment", "${e.message}")
            }
        }
    }

    override fun onStart() {
        (activity as AppCompatActivity)!!.supportActionBar?.show()
        super.onStart()
    }

    //location
    private fun getCurrentLocation() {
        if(checkPermissions()){
            if(isLocationEnabled()){
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(requireActivity()){ task->
                    val location : Location? = task.result
                    if(location == null){
                        Toast.makeText(requireContext(),"Location is not specified",Toast.LENGTH_LONG).show()
                        binding.textView8.visibility = VISIBLE
                    }else{
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        val cityName = addresses!![0].locality
                        getPost(cityName)
                    }

                }
            }else{
                Toast.makeText(requireContext(),"Turn on location",Toast.LENGTH_LONG).show()
                binding.textView8.visibility = VISIBLE
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        }else{
            requestPermission()
        }


    }

    private fun isLocationEnabled(): Boolean {
        val locationManager : LocationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }



    private fun requestPermission() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(requireContext(),"Granted",Toast.LENGTH_LONG).show()

            }else{
                Toast.makeText(requireContext(),"Not Granted",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkPermissions(): Boolean {
        if(ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    companion object{
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }


}