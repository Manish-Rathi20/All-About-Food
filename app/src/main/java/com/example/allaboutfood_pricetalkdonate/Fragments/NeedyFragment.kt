package com.example.allaboutfood_pricetalkdonate.Fragments

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.allaboutfood_pricetalkdonate.Adapter.NeedyAdapter
import com.example.allaboutfood_pricetalkdonate.Adapter.TalkAdapter
import com.example.allaboutfood_pricetalkdonate.Model.Needy
import com.example.allaboutfood_pricetalkdonate.Model.postss
import com.example.allaboutfood_pricetalkdonate.NeedyDataActivity
import com.example.allaboutfood_pricetalkdonate.databinding.FragmentNeedyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class NeedyFragment : Fragment() {
    private lateinit var auth : FirebaseAuth
    private lateinit var binding: FragmentNeedyBinding
    private lateinit var fireStoreDb : FirebaseFirestore
    private  var needyPost = mutableListOf<Needy>()
    private var post = mutableListOf<Needy>()
    private lateinit var adapter : NeedyAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNeedyBinding.inflate(layoutInflater)
        fireStoreDb = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()


        binding.fabNeedyButton.setOnClickListener{
          startActivity(Intent(activity,NeedyDataActivity::class.java))
        }

        getPost()
        adapter = NeedyAdapter(post)
        binding.rvNeedy.adapter = adapter
        binding.rvNeedy.layoutManager = LinearLayoutManager(activity)
        return binding.root
    }


    private fun getPost() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                fireStoreDb.collection("needy").orderBy("verified",Query.Direction.DESCENDING)
                    .addSnapshotListener { snapshot, exception ->
                        if (exception != null || snapshot == null) {
                            Toast.makeText(activity, "somthing went wrong", Toast.LENGTH_LONG).show()
                            Log.d("NeedyFragment", "${exception?.message}")
                            return@addSnapshotListener
                        }
                        needyPost = snapshot.toObjects(Needy::class.java)
                        post.clear()
                        post.addAll(needyPost)
                        adapter.notifyDataSetChanged()

                    }

            } catch (e: Exception) {
                Log.d("NeedyFragment", "${e.message}")
            }
        }
    }


    }




