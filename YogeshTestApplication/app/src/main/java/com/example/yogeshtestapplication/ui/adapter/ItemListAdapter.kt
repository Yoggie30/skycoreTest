package com.example.yogeshtestapplication.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.yogeshtestapplication.R
import com.example.yogeshtestapplication.model.BusinessesItem
import kotlinx.android.synthetic.main.item.view.*


class ItemListAdapter(
    var context: Context,
    var mList: ArrayList<BusinessesItem>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mSelectedPos = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NotificationListImgsViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val dataItem = mList[position]

        holder.itemView.tvTitle.text = dataItem.name

        val dis = dataItem.distance?.toInt().toString() + "m"
        val loc = dataItem.location?.display_address?.get(0)
        val des = "$dis, $loc"
        holder.itemView.tvDes1.text = des

        val des2 = if(dataItem.is_closed) "Currently CLOSED" else "Currently OPEN"
        holder.itemView.tvDes2.text = des2
        holder.itemView.tvRating.text = dataItem.rating.toString()

        holder.itemView.ivLogo.tvDes1
        Glide.with(context)
            .load(dataItem.image_url.toString())
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.itemView.ivLogo);
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }


    override fun getItemCount(): Int {
        return mList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    class NotificationListImgsViewHolder(view: View) : RecyclerView.ViewHolder(view)


}
