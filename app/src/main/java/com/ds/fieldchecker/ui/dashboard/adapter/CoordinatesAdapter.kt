package com.ds.fieldchecker.ui.dashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ds.fieldchecker.data.repo.model.CoordinatesItem
import com.ds.fieldchecker.databinding.CoordinateItemBinding

class CoordinatesAdapter(val context: Context,val listener:coordinateClickListiner) :
    RecyclerView.Adapter<CoordinatesAdapter.CoordinatesViewHolder>() {

    private var locationLists: ArrayList<CoordinatesItem>? = null

    fun setList(i: ArrayList<CoordinatesItem>?) {
        if (i == null) {
            return
        }
        locationLists = i
        notifyDataSetChanged()
    }

    interface coordinateClickListiner{
        fun onCoordinateClicked(coordinatesItem: CoordinatesItem)
        fun onLocationClicked(coordinatesItem: CoordinatesItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoordinatesViewHolder {
        val binding =
            CoordinateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoordinatesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoordinatesViewHolder, position: Int) {
        holder.bindDataToView(locationLists!![position])
    }

    override fun getItemCount(): Int {
        return locationLists?.size!!
    }

    inner class CoordinatesViewHolder(val view: CoordinateItemBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bindDataToView(coordinatesItem: CoordinatesItem) {
            view.locName.text=coordinatesItem.name
            if (coordinatesItem.status == true){
                view.status.text="Closed"
            }else view.status.text="Open"
            view.locTxt.text=""+coordinatesItem.taskLat+" "+coordinatesItem.taskLon
            view.itemLayout.setOnClickListener {
                listener.onCoordinateClicked(coordinatesItem)
            }
            view.locTxt.setOnClickListener{
                listener.onLocationClicked(coordinatesItem)
            }
        }
    }

}