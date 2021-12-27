package com.example.mypetyourpet

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mypetyourpet.fragments.*
import com.example.mypetyourpet.model.Channel
import com.example.mypetyourpet.model.Post
import com.google.android.material.bottomnavigation.BottomNavigationView

class SectionsActivity : AppCompatActivity(), CatalogFragment.OnPostSelectedListener
    , PostsFragment.OnPendingRequestListener, RequestsFragment.OnRequestListener , ChatFragment.OnChannelSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sections)
        getSupportActionBar()?.hide();
        val userId = getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE).getString("LOGIN_ID", "")!!;
        val ft = supportFragmentManager.beginTransaction();
        ft.replace(R.id.flaContent, CatalogFragment(userId));
        ft.commit();
        val bottom_nav: BottomNavigationView = findViewById(R.id.bottom_navigation);
        bottom_nav.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.catalog -> {
                    val ft = supportFragmentManager.beginTransaction();
                    ft.replace(R.id.flaContent, CatalogFragment(userId));
                    ft.commit();
                }
                R.id.posts -> {
                    val ft = supportFragmentManager.beginTransaction();
                    ft.replace(R.id.flaContent, PostsFragment(userId));
                    ft.commit();
                }
                R.id.requests -> {
                    val ft = supportFragmentManager.beginTransaction();
                    ft.replace(R.id.flaContent, RequestsFragment(userId));
                    ft.commit();
                }
                R.id.chat -> {
                    val ft = supportFragmentManager.beginTransaction();
                    ft.replace(R.id.flaContent, ChatFragment(userId));
                    ft.commit();
                }
            }

            true
        }

    }

    override fun onSelectInCatalog(post: Post) {

        val intent: Intent = Intent();
        intent.setClass(this, SelectPostActivity::class.java)
        intent.putExtra("id", post.id)
        intent.putExtra("name", post.name)
        intent.putExtra("species", post.species)
        intent.putExtra("description", post.description)
        intent.putExtra("image", post.image)
        intent.putExtra("state", post.state)
        intent.putExtra("userId", post.userId)
        startActivity(intent);
    }

    override fun onSelectPendingRequest(post: Post) {
        val intent: Intent = Intent();
        intent.setClass(this, SelectPendingRequestActivity::class.java)
        intent.putExtra("id", post.id)
        intent.putExtra("name", post.name)
        intent.putExtra("species", post.species)
        intent.putExtra("description", post.description)
        intent.putExtra("image", post.image)
        intent.putExtra("state", post.state)
        intent.putExtra("userId", post.userId)
        startActivity(intent);
    }

    override fun onSelectRequest(post: Post) {
        val intent: Intent = Intent();
        intent.setClass(this, SelectRequestActivity::class.java)
        intent.putExtra("id", post.id)
        intent.putExtra("name", post.name)
        intent.putExtra("species", post.species)
        intent.putExtra("description", post.description)
        intent.putExtra("image", post.image)
        intent.putExtra("state", post.state)
        intent.putExtra("userId", post.userId)
        startActivity(intent);
    }

    override fun onSelectChannel(channel: Channel) {
        val intent: Intent = Intent();
        intent.setClass(this, ChannelActivity::class.java)
        intent.putExtra("id", channel.id)
        intent.putExtra("CurrentUser", channel.users[0])
        intent.putExtra("OtherUser", channel.users[1])
        startActivity(intent);
    }
}

