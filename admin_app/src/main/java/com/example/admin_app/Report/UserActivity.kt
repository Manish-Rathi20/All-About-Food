package com.example.admin_app.Report

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_app.AdminAuth.AdminLoginActivity
import com.example.admin_app.NeedyPost.NeedyPeopleAdmin
import com.example.admin_app.Organization.OrgShow
import com.example.admin_app.Organization.Orgranization
import com.example.admin_app.R
import com.example.admin_app.SharedPreferencesManager
import com.example.admin_app.databinding.ActivityUserBinding

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import org.w3c.dom.Text


class UserActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserBinding
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var firebaseDb : FirebaseFirestore
    private lateinit var adapter: reportAdapter

    //on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SharedPreferencesManager.init(this)
        val sharedPref = SharedPreferencesManager.getSharedPreferences()
        val editor = sharedPref.edit()

        var username = sharedPref.getString("username","ADMIN")

        toggle = ActionBarDrawerToggle(this@UserActivity, binding.drawar,
            R.string.Open,
            R.string.Close
        )
        binding.drawar.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val header = binding.navDrawer.getHeaderView(0)
        header.findViewById<TextView>(R.id.userProfileName).text = username
        binding.navDrawer.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.addOrganization -> {
                    startActivity(Intent(this@UserActivity, Orgranization::class.java))
                }
                R.id.Organization -> {
                    startActivity(Intent(this@UserActivity, OrgShow::class.java))
                }
                R.id.adminLogout -> {
                    Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
                    editor.apply {
                        putBoolean("isLog",false)
                        apply()
                    }
                    startActivity(Intent(this@UserActivity,AdminLoginActivity::class.java))
                    finishAffinity()
                }
                R.id.NeedyPeopleMenu ->{
                    startActivity(Intent(this@UserActivity, NeedyPeopleAdmin::class.java))
                }
            }
            true
        }


        firebaseDb = FirebaseFirestore.getInstance()
        firebaseDb.collection("Report").orderBy("report_time",Query.Direction.DESCENDING)
            .addSnapshotListener{snapshot,exception->
             if(snapshot == null || exception != null){
                 Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show()
                 return@addSnapshotListener
             }
                val reports = snapshot.toObjects(UserReportData::class.java)
                adapter = reportAdapter(reports)
                binding.rvReportList.adapter = adapter
                binding.rvReportList.layoutManager = LinearLayoutManager(this)


            }





    }
    // onOptions
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        val sharedPref = SharedPreferencesManager.getSharedPreferences()
        val isLog = sharedPref.getBoolean("isLog",false)
        if(!isLog){
            startActivity(Intent(this@UserActivity,AdminLoginActivity::class.java))
            finishAffinity()
        }
  }
}