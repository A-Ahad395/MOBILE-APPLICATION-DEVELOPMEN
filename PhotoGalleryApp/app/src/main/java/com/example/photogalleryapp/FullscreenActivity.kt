package com.example.photogalleryapp

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class FullscreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        val imgFullscreen = findViewById<ImageView>(R.id.imgFullscreen)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        val imageRes = intent.getIntExtra("image", R.drawable.photo1)
        imgFullscreen.setImageResource(imageRes)

        btnBack.setOnClickListener {
            finish()
        }
    }
}