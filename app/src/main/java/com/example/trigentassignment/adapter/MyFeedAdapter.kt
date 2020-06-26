package com.example.trigentassignment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trigentassignment.R
import com.example.trigentassignment.model.FeedModel
import com.squareup.picasso.Picasso
import java.util.ArrayList

class MyFeedAdapter(private val context:Context, private val feed_data: ArrayList<FeedModel>):RecyclerView.Adapter<MyFeedAdapter.ViewHolder>(){

    inner class ViewHolder(view:View):RecyclerView.ViewHolder(view){
      val  tv_description = view.findViewById(R.id.tv_description) as TextView?
       val  tv_title = view.findViewById(R.id.tv_title) as TextView?
       val  iv_feed = view.findViewById(R.id.iv_feed) as ImageView?

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFeedAdapter.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.feed_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return feed_data.size
    }

    override fun onBindViewHolder(holder: MyFeedAdapter.ViewHolder, position: Int) {
        holder.tv_title?.text=(feed_data.get(position).title)
        holder.tv_description?.text=(feed_data.get(position).description)
        try {
            val image_url: String = feed_data.get(position).imageHref
            Picasso.with(context).load(image_url)
                .fit()
                .placeholder(R.drawable.image_holder)
                .into(holder.iv_feed)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}



