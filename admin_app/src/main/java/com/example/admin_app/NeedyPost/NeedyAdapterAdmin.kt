package com.example.admin_app.NeedyPost

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.core.text.bold
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.admin_app.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NeedyAdapterAdmin(val context: Context, val needyPeople: List<Needy>) :
    RecyclerView.Adapter<NeedyAdapterAdmin.NeedyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NeedyHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_org_needy_view, parent, false)
        return NeedyHolder(view)
    }

    override fun getItemCount(): Int {
        return needyPeople.size
    }

    //@RequiresApi(Build.VERSION_CODES.S)
    override fun onBindViewHolder(holder: NeedyHolder, position: Int) {

        //DataBind
        holder.city.text = needyPeople[position].address
        Glide
            .with(holder.context)
            .load(needyPeople[position].image_url)
            .placeholder(R.drawable.logonew)
            .into(holder.image)

        //verified or not
        if (needyPeople[position].verified == 1) {
            holder.verfiedSwitch.visibility = GONE
            holder.tvVerified.visibility = VISIBLE
            holder.tvVerified.setTextColor(Color.parseColor("#3BA840"))
        } else {
            holder.verfiedSwitch.visibility = VISIBLE
            holder.tvVerified.visibility = GONE
        }

        // on click on items
        holder.itemView.setOnClickListener {
            showDialog(holder, position)
        }


        val db = FirebaseFirestore.getInstance()
        holder.dropDownDelete.setOnClickListener{
            val popups = PopupMenu(holder.context,holder.dropDownDelete)
            popups.inflate(R.menu.needy_options)
            popups.setOnMenuItemClickListener {
            if(it.itemId == R.id.needyPostOption)  {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        db.collection("needy").whereEqualTo("desc", needyPeople[position].desc)
                            .get()
                            .addOnCompleteListener{task->
                                if(task.isSuccessful && !task.result.isEmpty){
                                    val documentsnapshot = task.result.documents[0]
                                    val documentId = documentsnapshot.id
                                    FirebaseFirestore.getInstance().collection("needy").document(documentId!!).delete()
                                    Toast.makeText(holder.context, "Successfully Deleted", Toast.LENGTH_LONG).show()
                                } else {
                                    Log.d("needyAdapterAdmin", task.exception.toString())
                                    Toast.makeText(
                                        holder.context,
                                        "Something went wrong ${task.exception}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                    }catch (e : Exception){
                        Toast.makeText(
                            holder.context,
                            "Something went wrong ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
            }
                true
            }
            popups.show()

        }

        holder.verfiedSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        db.collection("needy").whereEqualTo("desc", needyPeople[position].desc)
                            .addSnapshotListener { snapshot, exception ->
                                if (snapshot == null || exception != null) {
                                    Toast.makeText(holder.context, "${exception?.message}", Toast.LENGTH_LONG)
                                        .show()
                                    return@addSnapshotListener
                                }
                                val documentSnapshot = snapshot.documents[0]
                                val documentId = documentSnapshot.id
                                db.collection("needy").document(documentId).update("verified", 1)
                                    .addOnCompleteListener{
                                        if(it.isSuccessful){
                                            Toast.makeText(holder.context, "Updated", Toast.LENGTH_LONG).show()
                                            holder.verfiedSwitch.visibility = GONE
                                            holder.tvVerified.visibility = VISIBLE
                                            holder.tvVerified.setTextColor(Color.parseColor("#3BA840"))
                                        }
                                    }

                            }
                    } catch (e: Exception) {
                        Toast.makeText(
                            holder.context,
                            "Something went wrong ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            }
        }


    }




    private fun showDialog(holder: NeedyHolder, position: Int) {
        val dialog = Dialog(holder.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet)
        val imgView = dialog.findViewById<ImageView>(R.id.imgNeedyPhoto)
        val address = dialog.findViewById<TextView>(R.id.txtAddress)
        val Desc = dialog.findViewById<TextView>(R.id.txtDesc)
        val createdby = dialog.findViewById<TextView>(R.id.txtCreatedBy)
        val mobile = dialog.findViewById<TextView>(R.id.txtPhone)
        Glide
            .with(holder.context)
            .load(needyPeople[position].image_url)
            .placeholder(R.drawable.logonew)
            .into(imgView)

        SpannableStringBuilder().append("Address : ").bold { append(needyPeople[position].address) }
        address.text = SpannableStringBuilder().append("Address : ")
            .bold { append(needyPeople[position].address) }
        Desc.text =
            SpannableStringBuilder().append("Desc : ").bold { append(needyPeople[position].desc) }
        createdby.text = SpannableStringBuilder().append("Created By : ")
            .bold { append(needyPeople[position].created_by) }
        mobile.text = SpannableStringBuilder().append("Mobile : ")
            .bold { append(needyPeople[position].mobile.toString()) }


        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)
    }


    class NeedyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val city: TextView = itemView.findViewById(R.id.tvCityName)!!
        val image: ImageView = itemView.findViewById(R.id.imOrgPicture)!!
        val context = itemView.context!!
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        val verfiedSwitch = itemView.findViewById<Switch>(R.id.switch1)!!
        val tvVerified = itemView.findViewById<TextView>(R.id.tvVerified)!!
        val dropDownDelete = itemView.findViewById<ImageView>(R.id.deleteRequestNeedy)!!

    }


}

