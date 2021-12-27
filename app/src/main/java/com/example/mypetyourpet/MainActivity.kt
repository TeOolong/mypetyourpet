package com.example.mypetyourpet

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mypetyourpet.model.LoginManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSupportActionBar()?.hide();
        var eteUsername = findViewById<EditText>(R.id.eteUsername);
        var etePassword = findViewById<EditText>(R.id.etePassword);
        val butLogin: Button = findViewById(R.id.butLogin);
        butLogin.setOnClickListener {
            var username = ""
            if (eteUsername.text.length != 0) {
                username = eteUsername.text.toString()
            }
            var password = ""
            if (etePassword.text.length != 0) {
                password = etePassword.text.toString()
            }
            LoginManager.instance.checkLogin(
                username,
                password
            ) { resp, id ->
                if (resp != "") {
                    Toast.makeText(this, resp, Toast.LENGTH_LONG).show();
                } else {
                    val editor = getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE).edit()
                    editor.putString("LOGIN_ID", id)
                    editor.commit()

                    val intent: Intent = Intent();
                    intent.setClass(this, SectionsActivity::class.java)
                    startActivity(intent);
                }
            }

        }
        val butRegister: Button = findViewById(R.id.butRegister)
        butRegister.setOnClickListener {
            val intent: Intent = Intent();
            intent.setClass(this, RegisterActivity::class.java)
            startActivity(intent);
        }
    }
}