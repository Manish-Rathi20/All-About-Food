package com.example.admin_app.Report

import android.text.SpannableStringBuilder
import android.text.format.DateUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.example.admin_app.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class reportAdapter(val reportList: List<UserReportData>) :
    RecyclerView.Adapter<reportAdapter.MyReportHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReportHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_views, parent, false)
        return MyReportHolder(view)
    }

    override fun getItemCount() =
        reportList.size


    override fun onBindViewHolder(holder: MyReportHolder, position: Int) {

        holder.reportName.text = SpannableStringBuilder().append("Report : ").bold {
            append(
                reportList[position].reportText
            )
        }
        holder.reportPostId.text = SpannableStringBuilder().append("Post Id : ").bold {
            append(
                reportList[position].documentId
            )
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                FirebaseFirestore.getInstance().collection("posts")
                    .document(reportList[position].documentId)
                    .addSnapshotListener { snapshot, exception ->
                        if (snapshot == null || exception != null) {
                            Toast.makeText(
                                holder.context,
                                "Something went wrong",
                                Toast.LENGTH_LONG
                            ).show()
                            return@addSnapshotListener
                        }
                        val postData = snapshot.toObject(postss::class.java)
                        if (postData != null) {
                            holder.reportPostDesc.text = postData.desc
                            holder.reportPostTime.text =
                                DateUtils.getRelativeTimeSpanString(postData.creation_time)
                            holder.reportPostTitle.text = postData.title
                            holder.reportUserName.text = postData.user?.username!!

                        }

                    }
            } catch (e: Exception) {
                Toast.makeText(holder.context, "Something went wrong", Toast.LENGTH_LONG).show()
            }

        }

        holder.reportLayout.setOnClickListener {
            val popUp = PopupMenu(holder.context, holder.reportLayout, Gravity.END)
            popUp.inflate(R.menu.pop_up_report_menu)
            popUp.setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.deleteReport) {
                    deleteReport(holder, position)
                }

                if (item.itemId == R.id.deletePostReport) {
                    FirebaseFirestore.getInstance().collection("posts")
                        .document(reportList[position].documentId).delete()
                    deleteReport(holder, position)
                }

                true
            }
            popUp.show()
        }


    }

    private fun deleteReport(holder: MyReportHolder, position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            FirebaseFirestore.getInstance().collection("Report")
                .whereEqualTo("reportText", reportList[position].reportText)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && !task.result.isEmpty) {
                        val documentsnapshot = task.result.documents[0]
                        val documentId = documentsnapshot.id
                        FirebaseFirestore.getInstance().collection("Report").document(documentId!!)
                            .delete()
                        Toast.makeText(holder.context, "Successfully Deleted", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        Log.d("reportAdapter", task.exception.toString())
                        Toast.makeText(
                            holder.context,
                            "Something went wrong ${task.exception}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        holder.context,
                        "Something went wrong ${it.message}",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d("reportAdapter", it.message.toString())

                }

        }

    }

    class MyReportHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val reportName = itemView.findViewById<TextView>(R.id.reportName)!!
        val reportPostId =
            itemView.findViewById<TextView>(R.id.reportPostId)!!
        val reportUserProfile = itemView.findViewById<ImageView>(R.id.reportUserProfileImage)!!
        val reportUserName =
            itemView.findViewById<TextView>(R.id.reportUserName)!!
        val reportPostTime =
            itemView.findViewById<TextView>(R.id.reportPostTime)!!
        val reportPostTitle =
            itemView.findViewById<TextView>(R.id.reportPostTitle)!!
        val reportPostDesc =
            itemView.findViewById<TextView>(R.id.reportPostDesc)!!
        val context = itemView.context!!
        val reportLayout = itemView.findViewById<LinearLayout>(R.id.reportLinearLayout)!!
    }
}