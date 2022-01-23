package com.example.visahub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.visahub.R
import com.example.visahub.data.VisaDetails
import kotlinx.android.synthetic.main.item_available_visa.view.*

class AvailableVisaAdapter(private val visaList: ArrayList<VisaDetails>) : RecyclerView.Adapter<AvailableVisaAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AvailableVisaAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_available_visa, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AvailableVisaAdapter.ViewHolder, position: Int) {
        val currentItem = visaList[position]

        Glide
            .with(holder.itemView.context)
            .load(currentItem.imageUrl)
            .centerCrop()
            .placeholder(R.drawable.circle_profile)
            .error(R.drawable.circle_profile)
            .into(holder.visaImage)

        holder.visaType.text = currentItem.name
        holder.continent.text = currentItem.continent
        holder.currency.text = currentItem.currency
        holder.validity.text = currentItem.validity
        holder.processing.text = currentItem.processingTime
    }

    override fun getItemCount(): Int {
        return visaList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val visaImage = itemView.visa_image_view
        val visaType = itemView.visa_Type
        val continent = itemView.continent
        val currency = itemView.currency
        val validity = itemView.validity
        val processing = itemView.processing_time
    }
}