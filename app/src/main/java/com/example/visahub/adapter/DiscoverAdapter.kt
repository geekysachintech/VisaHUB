package com.example.visahub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.visahub.R
import com.example.visahub.data.DiscoverFeedData
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.discover_item_view.view.*

class DiscoverAdapter(private val context: Context) : RecyclerView.Adapter<DiscoverAdapter.ViewHolder>() {

    private val dummyDataArray: ArrayList<DiscoverFeedData> = arrayListOf(
        DiscoverFeedData(imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/India_Gate_at_night_%2C_New_Delhi.jpg/400px-India_Gate_at_night_%2C_New_Delhi.jpg", title = "India"),
        DiscoverFeedData(imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/India_Gate_at_night_%2C_New_Delhi.jpg/400px-India_Gate_at_night_%2C_New_Delhi.jpg", title = "India"),
        DiscoverFeedData(imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/India_Gate_at_night_%2C_New_Delhi.jpg/400px-India_Gate_at_night_%2C_New_Delhi.jpg", title = "India"),
        DiscoverFeedData(imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/ec/India_Gate_at_night_%2C_New_Delhi.jpg/400px-India_Gate_at_night_%2C_New_Delhi.jpg", title = "India"),
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.discover_item_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiscoverAdapter.ViewHolder, position: Int) {
        Picasso.with(context)
            .load(dummyDataArray[position].imageUrl)
            .placeholder(R.drawable.circle_profile) //optional
            .resize(150, 250)         //optional
            .centerCrop()                        //optional
            .into(holder.imageView)

        holder.textView.text = dummyDataArray[position].title
    }

    override fun getItemCount(): Int {
        return dummyDataArray.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val imageView = itemView.discover_image_view
        val textView = itemView.discover_title_text_view
    }
}