package com.example.mypetyourpet

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mypetyourpet.model.ChatManager
import com.example.mypetyourpet.model.Post
import com.example.mypetyourpet.model.RequestsManager
import java.net.URLEncoder

class SelectRequestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailedrequest)
        getSupportActionBar()?.hide();
        val userId = getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE).getString("LOGIN_ID", "")!!;
        var data = intent.extras;
        var post = Post(data!!.getLong("id"),
            data.getString("name")!!,
            data.getString("species")!!,
            data.getString("description")!!,
            data.getString("image")!!,
            data.getString("state")!!,
            data.getString("userId")!!)

        var iviPost = findViewById<ImageView>(R.id.iviPost);
        var tviName = findViewById<TextView>(R.id.tviName);
        var tviDescription = findViewById<TextView>(R.id.tviDescription);

        val butContact : Button = findViewById(R.id.butContact)
        RequestsManager.instance.getRequestByPost(post.userId, post.id) { req ->
            butContact.setOnClickListener{
                ChatManager.instance.createChannel(req.id.toString(),userId, req.publisherId) {

                    if(it) {
                        Toast.makeText(this, "Chat ya ha sido creado", Toast.LENGTH_LONG).show();
                    }
                }
                onBackPressed()
            }

        }



        val butCancel : Button = findViewById(R.id.butCancel)
        butCancel.setOnClickListener{
            onBackPressed()
        }
        tviName.text = post.name;
        tviDescription.text = post.description
        Glide.with(this).load(post.image)
            .override(800, 900)
            .centerCrop()
            .into(iviPost)

    }

}