package com.example.allaboutfood_pricetalkdonate.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.allaboutfood_pricetalkdonate.FlipKartWeb
import com.example.allaboutfood_pricetalkdonate.R

class ItemDataAdapter(val itemResult : List<com.example.allaboutfood_pricetalkdonate.Model.Result>) : RecyclerView.Adapter<ItemDataAdapter.MyViewModel>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewModel {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_flipkart_view, parent, false)
        return MyViewModel(view)
    }

    override fun getItemCount(): Int {
        return itemResult.size
    }


    override fun onBindViewHolder(holder: MyViewModel, position: Int) {
        holder.product_name.text = itemResult[position].name
        holder.product_price.text = "Rs. ${itemResult[position].current_price}"
       // NumberFormat.getCurrencyInstance(Locale("en","in")).format(holder.product_price)

        Glide
            .with(holder.context)
            .load(itemResult[position].thumbnail.toString())
            .placeholder(R.drawable.logonew)
            .into(holder.product_image)

        holder.itemView.setOnClickListener{
            val intent = Intent(holder.context,FlipKartWeb::class.java)
            intent.putExtra("itemLink",itemResult[position].link)
            holder.context.startActivity(intent)
        }


    }
    class MyViewModel(itemView: View) : RecyclerView.ViewHolder(itemView) {
     val product_name = itemView.findViewById<TextView>(R.id.itemName)!!
        val product_price = itemView.findViewById<TextView>(R.id.itemPrice)!!
        val product_image = itemView.findViewById<ImageView>(R.id.itemPhotoImage)!!
        val context = itemView.context!!
    }
}
