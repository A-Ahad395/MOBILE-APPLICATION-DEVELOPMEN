package com.example.photogalleryapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class PhotoAdapter(
    private val context: Context,
    private val photos: List<Int>
) : BaseAdapter() {
    private val photoTitles = listOf(
        "Anime Boy",
        "Dark Portrait",
        "Cute Girl",
        "Power Look",
        "Red Theme",
        "Green Theme",
        "Hoodie Girl",
        "Soft Art",
        "Moon Night",
        "Orange Theme",
        "Blue Theme",
        "Maid Art"
    )

    override fun getCount(): Int = photos.size

    override fun getItem(position: Int): Any = photos[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_photo, parent, false)

        val imgPhoto = view.findViewById<ImageView>(R.id.imgPhoto)
        val tvPhotoTitle = view.findViewById<TextView>(R.id.tvPhotoTitle)
        imgPhoto.setImageResource(photos[position])
        tvPhotoTitle.text = photoTitles[position]

        return view
    }
}