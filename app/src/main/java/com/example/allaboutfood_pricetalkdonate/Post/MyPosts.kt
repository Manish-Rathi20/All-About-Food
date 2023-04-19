package com.example.allaboutfood_pricetalkdonate.Post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.allaboutfood_pricetalkdonate.Adapter.TalkAdapter
import com.example.allaboutfood_pricetalkdonate.Model.postss
import com.example.allaboutfood_pricetalkdonate.databinding.ActivityMyPostsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyPosts : AppCompatActivity(){
    private lateinit var binding : ActivityMyPostsBinding
    private lateinit var firestoreDb : FirebaseFirestore
    private lateinit var adapter: TalkAdapter
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPostsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        firestoreDb = FirebaseFirestore.getInstance()
        getMyPost()
    }

    private fun getMyPost() {
     CoroutineScope(Dispatchers.IO).launch {
         try {
             var postReference = firestoreDb.collection("posts")
                 .orderBy("creation_time", Query.Direction.DESCENDING)

             postReference = postReference.whereEqualTo("user.email",auth.currentUser?.email)
             //postReference = postReference.whereEqualTo("user.username",signInUser?.username)

             postReference.addSnapshotListener{snapshot,exception->
                 if(snapshot == null || exception != null){
                     Log.d("PostActivity","${exception?.message}")
                     Toast.makeText(this@MyPosts,"${exception?.message}",Toast.LENGTH_SHORT).show()
                        return@addSnapshotListener
                 }
                 var myPost = snapshot.toObjects(postss::class.java)
                 adapter = TalkAdapter(this@MyPosts, myPost)
                 binding.rvMyPost.adapter = adapter
                 binding.rvMyPost.layoutManager = LinearLayoutManager(this@MyPosts)
                 adapter.notifyDataSetChanged()
             }

         }catch(e : Exception){
             withContext(Dispatchers.Main){
                 Toast.makeText(this@MyPosts,"Something Went Wrong",Toast.LENGTH_SHORT).show()
             }
         }
     }
    }
}