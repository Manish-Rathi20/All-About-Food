package com.example.allaboutfood_pricetalkdonate.Adapter

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.view.View.GONE
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.allaboutfood_pricetalkdonate.Model.organizationData
import com.example.allaboutfood_pricetalkdonate.R

class OrgAdapter(val orgList : List<organizationData>) : RecyclerView.Adapter<OrgAdapter.OrgHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrgHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_org_needy_view,parent,false)
        return OrgHolder(view)
    }

    override fun getItemCount(): Int {
        return orgList.size
    }

    override fun onBindViewHolder(holder: OrgHolder, position: Int) {
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

    }

    class OrgHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val OrgCity = itemView.findViewById<TextView>(R.id.tvCityName)
        val OrgPhoto = itemView.findViewById<ImageView>(R.id.imOrgPicture)
        val context = itemView.context
    }

}