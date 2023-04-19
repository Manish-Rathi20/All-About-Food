package com.example.admin_app.NeedyPost

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_app.databinding.ActivityNeedyPeopleAdminBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NeedyPeopleAdmin : AppCompatActivity() {
    private lateinit var binding: ActivityNeedyPeopleAdminBinding
    private lateinit var fireStoreDb : FirebaseFirestore
    private  var needyPost = mutableListOf<Needy>()
    private var post = mutableListOf<Needy>()
    private lateinit var adapter : NeedyAdapterAdmin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNeedyPeopleAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Needy People"
        fireStoreDb = FirebaseFirestore.getInstance()


        getPost()
        adapter = NeedyAdapterAdmin(this@NeedyPeopleAdmin, post)
        binding.rvNeedy.adapter = adapter
        binding.rvNeedy.layoutManager = LinearLayoutManager(this@NeedyPeopleAdmin)


    }
    private fun getPost() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                fireStoreDb.collection("needy")
                    .addSnapshotListener { snapshot, exception ->
                        if (exception != null || snapshot == null) {
                            Toast.makeText(this@NeedyPeopleAdmin, "somthing went wrong", Toast.LENGTH_LONG).show()
                            Log.d("NeedyFragment", "${exception?.message}")
                            return@addSnapshotListener
                        }
                        needyPost = snapshot.toObjects(Needy::class.java)
                        Log.d("NeedyActivity",needyPost.toString())
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