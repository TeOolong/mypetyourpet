package com.example.mypetyourpet

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
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

class SelectPendingRequestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pendingrequest)
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

        var tviTitle = findViewById<TextView>(R.id.tviTitle);
        var iviPost = findViewById<ImageView>(R.id.iviPost);
        var tviName = findViewById<TextView>(R.id.tviName);

        var butAccept = findViewById<Button>(R.id.butAccept);

        var butDecline = findViewById<Button>(R.id.butDecline);

        if(post.state == "finished") {
            butAccept.setVisibility(View.INVISIBLE)
            butDecline.setVisibility(View.INVISIBLE)
        }
        var butContact = findViewById<Button>(R.id.butContact);

        var butCancel = findViewById<Button>(R.id.butCancel);
        butCancel.setOnClickListener {
            onBackPressed()
        }

        RequestsManager.instance.getRequestByPost(userId, post.id) { req ->
            RequestsManager.instance.getRequesterData(req.requesterId) { user ->
                if(post.state != "finished") {
                    tviTitle.text = "${user.username} esta dispuesto a adoptar a".toUpperCase();
                }
                else {
                    tviTitle.text = "${user.username} ha adoptado a".toUpperCase();
                }

                Glide.with(this).load(post.image)
                    .override(800, 900)
                    .centerCrop()
                    .into(iviPost)
                tviName.text = post.name;

                butContact.setOnClickListener {
                    ChatManager.instance.createChannel(req.id.toString(),userId, user.id.toString()) {
                        if(it) {
                            Toast.makeText(this, "Chat ya ha sido creado", Toast.LENGTH_LONG).show();
                        }
                    }
                    onBackPressed()
                }
                butContact.text = "CONTACTAR ${user.username}"

            }
            butAccept.setOnClickListener {
                RequestsManager.instance.acceptRequest(req) {
                    onBackPressed()
                }

            }
            butDecline.setOnClickListener {
                RequestsManager.instance.deleteRequest(req) {
                    onBackPressed()
                }

            }

        }

            // Starting Whatsapp



    }
}