package com.example.admin_app.Organization

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.admin_app.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrganizationAdapter(val orgList : List<orgData>) : RecyclerView.Adapter<OrganizationAdapter.OrgHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrgHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_org_needy_view,parent,false)
        return OrgHolder(view)
    }

    override fun getItemCount(): Int {
        return orgList.size
    }

    override fun onBindViewHolder(holder: OrgHolder, position: Int) {
        holder.switch.visibility = GONE
        holder.OrgCity.text = orgList[position].organizationCity
        Glide
            .with(holder.context)
            .load(orgList[position].image_Url)
            .placeholder(R.drawable.logonew)
            .into(holder.OrgPhoto)

        holder.itemView.setOnClickListener {
            var dialog = Dialog(holder.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.bottom_sheet)
            val imgView = dialog.findViewById<ImageView>(R.id.imgNeedyPhoto)
            val address = dialog.findViewById<TextView>(R.id.txtAddress)
            val Desc = dialog.findViewById<TextView>(R.id.txtDesc)
            val createdby = dialog.findViewById<TextView>(R.id.txtCreatedBy)
            val mobile = dialog.findViewById<TextView>(R.id.txtPhone)
            Glide
                .with(holder.context)
                .load(orgList[position].image_Url)
                .placeholder(R.drawable.logonew)
                .into(imgView)
            address.text = orgList[position].organizationAdd
            Desc.text = orgList[position].organizationCity
            createdby.text = ""
            mobile.text = orgList[position].organizationMobile


            dialog.show()
            dialog.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation;
            dialog.window?.setGravity(Gravity.BOTTOM)
        }
        val db : FirebaseFirestore = FirebaseFirestore.getInstance()
        holder.dropdownButton.setOnClickListener{
                val popups = PopupMenu(holder.context,holder.dropdownButton)
                popups.inflate(R.menu.org_options)
                popups.setOnMenuItemClickListener {
                    if(it.itemId == R.id.orgDelete)  {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                db.collection("Org").whereEqualTo("organizationCity", orgList[position].organizationCity)
                                    .get()
                                    .addOnCompleteListener{task->
                                        if(task.isSuccessful && !task.result.isEmpty){
                                            val documentsnapshot = task.result.documents[0]
                                            val documentId = documentsnapshot.id
                                            FirebaseFirestore.getInstance().collection("Org").document(
                                                documentId
                                            ).delete()
                                            Toast.makeText(holder.context, "Successfully Deleted", Toast.LENGTH_LONG).show()
                                        } else {
                                            Log.d("OrgAdapter", task.exception.toString())
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

    }

    class OrgHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val OrgCity = itemView.findViewById<TextView>(R.id.tvCityName)
        val OrgPhoto = itemView.findViewById<ImageView>(R.id.imOrgPicture)
        val dropdownButton = itemView.findViewById<ImageView>(R.id.deleteRequestNeedy)
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        val switch = itemView.findViewById<Switch>(R.id.switch1)
        val context = itemView.context
    }

}