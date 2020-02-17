package com.example.android.sampleproject.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.android.sampleproject.R
import com.example.android.sampleproject.model.Bloggers
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_blogger_detail.*

class BloggerDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blogger_detail)

        val blog = intent.getSerializableExtra("EXTRA") as? Bloggers

        Picasso.get().load(blog?.image).into(detail_img)
        detail_title.text = blog?.title
        detail_about.text = blog?.about
    }
}
