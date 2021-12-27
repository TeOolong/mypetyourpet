package com.example.mypetyourpet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mypetyourpet.model.LoginManager

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        getSupportActionBar()?.hide();

        var eteUsername = findViewById<EditText>(R.id.eteUsername);
        var etePassword = findViewById<EditText>(R.id.etePassword);
        var etePhone = findViewById<EditText>(R.id.etePhone);
        val butRegister : Button = findViewById(R.id.butRegister)
        butRegister.setOnClickListener {
            var username = ""
            if(eteUsername.text.length != 0) {
                username = eteUsername.text.toString()
            }
            var password = ""
            if(etePassword.text.length != 0) {
                password = etePassword.text.toString()
            }
            var celular = "0"
            if(etePhone.text.length != 0) {
                celular = etePhone.text.toString()
            }

            LoginManager.instance.checkRegister(username,
                password, celular.toLong()
            ) {
                if(it!=""){
                    Toast.makeText(this, it, Toast.LENGTH_LONG).show();
                }
                else {
                    LoginManager.instance.saveUser(eteUsername.text.toString(),
                        etePassword.text.toString(), etePhone.text.toString())
                    val intent : Intent = Intent ();
                    intent.setClass(this,MainActivity::class.java)
                    startActivity(intent);
                }

            }
        }
    }





}