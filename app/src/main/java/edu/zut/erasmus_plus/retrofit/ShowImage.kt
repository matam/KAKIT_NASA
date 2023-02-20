package edu.zut.erasmus_plus.retrofit

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide

class ShowImage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_image)

        val image : ImageView = findViewById(R.id.showImageAPD)
        Glide.with(this)
            .load(intent.getStringExtra("url"))
            .skipMemoryCache(false)//for caching the image url in case phone is offline
            .into(image)

    }
}