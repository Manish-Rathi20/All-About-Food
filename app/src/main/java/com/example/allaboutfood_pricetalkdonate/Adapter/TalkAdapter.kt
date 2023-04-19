package com.example.allaboutfood_pricetalkdonate.Adapter

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.format.DateUtils
import android.view.*
import android.view.View.GONE
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.allaboutfood_pricetalkdonate.Model.UserReportData
import com.example.allaboutfood_pricetalkdonate.Model.postss
import com.example.allaboutfood_pricetalkdonate.Post.MyPosts
import com.example.allaboutfood_pricetalkdonate.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TalkAdapter(var context: Context, var postList: List<postss>) :
    RecyclerView.Adapter<TalkAdapter.MyViewModel>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewModel {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_view, parent, false)
        return MyViewModel(view)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: MyViewModel, position: Int) {
        holder.username.text = postList[position].user?.username
        holder.heading.text = postList[position].title
        holder.desc.text = postList[position].desc
        holder.creation_time.text =
            DateUtils.getRelativeTimeSpanString(postList[position].creation_time)
        Glide.with(context)
            .load(postList[position].user?.picUrl)
            .into(holder.userProfileImage)
        // document id
        val db = FirebaseFirestore.getInstance()
        var documentId: String? = null
        CoroutineScope(Dispatchers.IO).launch {
            db.collection("posts").whereEqualTo("desc", postList[position].desc)
                .get().addOnCompleteListener {
                    if (it.isSuccessful && !it.result.isEmpty) {
                        val documentSnapshot = it.result.documents[0]
                        documentId = documentSnapshot.id
                    } else {
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG)
                            .show()
                    }
                }

        }
        // document id
        if (context is MyPosts) {
            holder.imgList.visibility = GONE
            holder.itemView.setOnClickListener {

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Do you want to delete?")
                    .setPositiveButton("Yes") { _, _ ->
                        if (documentId != null) {
                            db.collection("posts").document(documentId!!).delete()
                            Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show()
                        }
                    }
                    .setNegativeButton("No") { _, _ ->

                    }.create().show()


            }


        }


        if (context !is MyPosts) {

            if(FirebaseAuth.getInstance().currentUser?.email == postList[position].user?.email){
                    holder.imgList.visibility = GONE
            }
            holder.imgList.setOnClickListener {
                val popup = PopupMenu(context, holder.imgList)
                popup.inflate(R.menu.pop_up_menu)
                popup.setOnMenuItemClickListener {
                    if (it.itemId == R.id.report) {
                        val dialog = Dialog(holder.context)
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                        dialog.setContentView(R.layout.report_pop_layout)

                        val reportSubmit = dialog.findViewById<Button>(R.id.reportSubmit)
                        reportSubmit.setOnClickListener{
                            val radioGroup = dialog.findViewById<RadioGroup>(R.id.reportRadioGroup)
                            val radioid = radioGroup.checkedRadioButtonId
                            val radiobutton : RadioButton = dialog.findViewById(radioid)
                            val reportData = UserReportData(documentId!! , radiobutton.text.toString(),System.currentTimeMillis())
                            FirebaseFirestore.getInstance().collection("Report")
                                .document().set(reportData)
                                .addOnCompleteListener{task->
                                    if(task.isSuccessful){
                                        Toast.makeText(holder.context,"Your report has been submitted",Toast.LENGTH_SHORT).show()
                                        dialog.hide()
                                    }else{
                                        Toast.makeText(holder.context,"Something went wrong + ${task.exception}",Toast.LENGTH_SHORT).show()

                                    }
                                }
                                .addOnFailureListener{
                                    Toast.makeText(holder.context,"Something went wrong + ${it.message}",Toast.LENGTH_SHORT).show()
                                }

                        }


                        dialog.show()
                        dialog.window?.setLayout(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
                        dialog.window?.setGravity(Gravity.BOTTOM)

                    }
                    true
                }
                popup.show()
            }
        }
    }

    class MyViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username = itemView.findViewById<TextView>(R.id.postUserName)!!
        val heading = itemView.findViewById<TextView>(R.id.postHeading)!!
        val desc = itemView.findViewById<TextView>(R.id.postDesc)!!
        val creation_time = itemView.findViewById<TextView>(R.id.creation_time)!!
        val userProfileImage = itemView.findViewById<ImageView>(R.id.profile_image)!!
        val context = itemView.context!!
        val imgList = itemView.findViewById<ImageButton>(R.id.imgList)!!
    }
}