package com.example.allaboutfood_pricetalkdonate.Fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allaboutfood_pricetalkdonate.Post.CreatePostActivity
import com.example.allaboutfood_pricetalkdonate.Adapter.TalkAdapter
import com.example.allaboutfood_pricetalkdonate.Model.postss
import com.example.allaboutfood_pricetalkdonate.databinding.FragmentTalkBinding
import com.example.allaboutfood_pricetalkdonate.network
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.*

class TalkFragment : Fragment() {
    private lateinit var binding: FragmentTalkBinding
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

        if(networkTalk?.isNetworkRequest() == true) {
            auth = FirebaseAuth.getInstance()

            fireStoreDb = FirebaseFirestore.getInstance()
            getPost()
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

    private fun getPost() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                fireStoreDb.collection("posts")
                    .orderBy("creation_time",Query.Direction.DESCENDING)
                    .addSnapshotListener { snapshot, exception ->
                    if (exception != null || snapshot == null) {
                        Toast.makeText(activity, "somthing went wrong", Toast.LENGTH_LONG).show()
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


}