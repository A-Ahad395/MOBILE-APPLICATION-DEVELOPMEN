package com.example.photogalleryapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.GridView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var gridPhotos: GridView
    private lateinit var selectionToolbar: LinearLayout
    private lateinit var tvSelectedCount: TextView

    private val photoList = listOf(
        R.drawable.photo1,
        R.drawable.photo2,
        R.drawable.photo3,
        R.drawable.photo4,
        R.drawable.photo5,
        R.drawable.photo6,
        R.drawable.photo7,
        R.drawable.photo8,
        R.drawable.photo9,
        R.drawable.photo10,
        R.drawable.photo11,
        R.drawable.photo12
    )

    private var selectedCount = 0
    private var selectionMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridPhotos = findViewById(R.id.gridPhotos)
        selectionToolbar = findViewById(R.id.selectionToolbar)
        tvSelectedCount = findViewById(R.id.tvSelectedCount)

        gridPhotos.adapter = PhotoAdapter(this, photoList)

        gridPhotos.setOnItemClickListener { _, _, position, _ ->
            if (!selectionMode) {
                val intent = Intent(this, FullscreenActivity::class.java)
                intent.putExtra("image", photoList[position])
                startActivity(intent)
            }
        }

        gridPhotos.setOnItemLongClickListener { _, _, _, _ ->
            selectionMode = true
            selectedCount = 1
            selectionToolbar.visibility = View.VISIBLE
            tvSelectedCount.text = "$selectedCount selected"
            true
        }
    }
}