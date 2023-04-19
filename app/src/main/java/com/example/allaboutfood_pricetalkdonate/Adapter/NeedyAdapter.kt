package com.example.allaboutfood_pricetalkdonate.Adapter


import android.app.Dialog
import android.graphics.Color
import android.graphics.RenderEffect
import android.graphics.RenderEffect.createBlurEffect
import android.graphics.Shader
import android.graphics.drawable.ColorDrawable
import android.icu.text.CaseMap.Title
import android.os.Build
import android.view.*
import android.view.View.VISIBLE
import android.view.View.X
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.allaboutfood_pricetalkdonate.Model.Needy
import com.example.allaboutfood_pricetalkdonate.R
import jp.wasabeef.blurry.Blurry


class NeedyAdapter(val needyPeople: List<Needy>) :
    RecyclerView.Adapter<NeedyAdapter.NeedyHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NeedyHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_org_needy_view, parent, false)
        return NeedyHolder(view)
    }

    override fun getItemCount(): Int {
        return needyPeople.size
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onBindViewHolder(holder: NeedyHolder, position: Int) {

        holder.city.text = needyPeople[position].address

        Glide
            .with(holder.context)
            .load(needyPeople[position].image_url)
            .placeholder(R.drawable.logonew)
            .into(holder.image)

        if (needyPeople[position].verified == 0) {
            holder.clock.visibility = VISIBLE
            holder.image.setRenderEffect(
                createBlurEffect(
                    20.0f, 20.0f, Shader.TileMode.CLAMP
                )
            )
        } else {
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
                    .load(needyPeople[position].image_url)
                    .placeholder(R.drawable.logonew)
                    .into(imgView)
                address.text = needyPeople[position].address
                Desc.text = needyPeople[position].desc
                createdby.text = needyPeople[position].created_by
                mobile.text = needyPeople[position].mobile.toString()


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

    }


    class NeedyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val city: TextView = itemView.findViewById(R.id.tvCityName)
        val image: ImageView = itemView.findViewById(R.id.imOrgPicture)
        val clock = itemView.findViewById<ImageView>(R.id.imClock)
        val cardView = itemView.findViewById<CardView>(R.id.needyCardView)!!
        val context = itemView.context
    }


}