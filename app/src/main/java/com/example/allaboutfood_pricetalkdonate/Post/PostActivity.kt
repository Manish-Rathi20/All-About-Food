package com.example.allaboutfood_pricetalkdonate.Post


import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.bumptech.glide.Glide
import com.example.allaboutfood_pricetalkdonate.Authentication.LoginActivity
import com.example.allaboutfood_pricetalkdonate.EditUserDetails
import com.example.allaboutfood_pricetalkdonate.Model.User
import com.example.allaboutfood_pricetalkdonate.R
import com.example.allaboutfood_pricetalkdonate.databinding.ActivityPostBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.allaboutfood_pricetalkdonate.network


open class PostActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var signInUser: User? = null
    lateinit var navController: NavController
    private lateinit var header: View
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityPostBinding
    private lateinit var firestoreDb: FirebaseFirestore

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Food Posts"
        auth = FirebaseAuth.getInstance()
        firestoreDb = FirebaseFirestore.getInstance()

        val networkPost = network(this)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val btmNavigationView = findViewById<BottomNavigationView>(R.id.btmNavigationView)
        setupWithNavController(btmNavigationView, navController)

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navDrawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.logout -> {
                    auth.signOut()
                    startActivity(Intent(this@PostActivity, LoginActivity::class.java))
                    finish()
                }
                R.id.Settings -> {
                    Toast.makeText(this@PostActivity, "Settings", Toast.LENGTH_SHORT).show()
                }
                R.id.editDetails -> {
                    startActivity(Intent(this@PostActivity, EditUserDetails::class.java))
                }
                R.id.MyPost ->{
                    startActivity(Intent(this@PostActivity, MyPosts::class.java))
                }
            }
            true
        }
      if(networkPost.isNetworkRequest()) {
          header = binding.navDrawer.getHeaderView(0)
          CoroutineScope(Dispatchers.IO).launch {
              try {
                  firestoreDb.collection("users").document(auth.currentUser?.uid as String)
                      .addSnapshotListener { snapshot, exception ->
                          if (exception != null || snapshot == null) {
                              Log.d("PostActivity", "exception is there", exception)
                              return@addSnapshotListener
                          }
                          signInUser = snapshot.toObject(User::class.java)
                          Glide
                              .with(this@PostActivity)
                              .load(signInUser?.picUrl)
                              .into(header.findViewById(R.id.userImage))
                          header.findViewById<TextView>(R.id.userEmailId).text =
                              auth.currentUser!!.email
                          header.findViewById<TextView>(R.id.userProfileName).text =
                              signInUser?.username

                      }
              } catch (e: Exception) {
                  Log.d("PostActivity", e.message.toString())
              }
          }
      }else{
          AlertDialog.Builder(this).setTitle("No Internet Connection")
              .setMessage("Please check your internet connection and try again")
              .setPositiveButton(android.R.string.ok) { dialog,which ->
                  this.recreate()
              }
              .setIcon(android.R.drawable.ic_dialog_alert).show()
      }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

//    @RequiresApi(Build.VERSION_CODES.M)
//    fun isNetworkRequest():Boolean{
//        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val activeNetwork = connectivityManager.activeNetwork
//        var networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
//        return (networkCapabilities != null) && networkCapabilities.hasCapability(
//            NetworkCapabilities.NET_CAPABILITY_INTERNET
//        )
//
//
//    }
}