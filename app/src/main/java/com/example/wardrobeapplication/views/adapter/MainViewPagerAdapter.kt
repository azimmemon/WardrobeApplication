package com.example.wardrobeapplication.views.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wardrobeapplication.R
import com.example.wardrobeapplication.model.PairDataModel
import kotlinx.android.synthetic.main.pair_single_row_view.view.*


class MainViewPagerAdapter(var ctx: Context): RecyclerView.Adapter<MainViewPagerAdapter.ViewHolder>() {

     var mDataList = mutableListOf<PairDataModel>()

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(ctx).inflate(R.layout.pair_single_row_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.pair_single_image.setImageBitmap(stringToBitmap(mDataList[position].imageString))
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    fun addAllItems(data: List<PairDataModel>){
        mDataList.clear()
        mDataList.addAll(data)
        notifyDataSetChanged()
    }

    fun stringToBitmap(imageString: String): Bitmap?{
        return try {
            val encodeByte: ByteArray = Base64.decode(imageString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
            bitmap
        } catch (e: Exception) {
            e.message
            null
        }
    }


}