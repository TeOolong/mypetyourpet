package com.example.mypetyourpet

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mypetyourpet.adapter.ChatListAdapter
import com.example.mypetyourpet.model.ChatManager

class ChannelActivity : AppCompatActivity() {

    private var img: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channelsession)
        getSupportActionBar()?.hide();
        createNotificationChannel("1");
        val userId = getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE).getString("LOGIN_ID", "")!!;
        var data = intent.extras;
        var channel = data!!.getLong("id").toString()
        var scroll = findViewById<ScrollView>(R.id.scrollMessages);


        var imbSendImage = findViewById<ImageButton>(R.id.imbSendImage);
        imbSendImage.setOnClickListener {
            getImage()
        }
        var eteMessage = findViewById<EditText>(R.id.eteMessage);
        var imbSend = findViewById<ImageButton>(R.id.imbSend);

        imbSend.setOnClickListener {
            if(eteMessage.text.toString() != "") {
                ChatManager.instance.addMessageToChannel(channel, eteMessage.text.toString(),userId, "TEXT" )
                eteMessage.setText("");

            }

        }
        realTimeUpdatesMessages(channel,userId)


    }
    fun realTimeUpdatesMessages(channel : String,userId: String ) {
        ChatManager.instance.addListenerMessageToChannel(channel) {
            var rviMessages = findViewById<RecyclerView>(R.id.rviMessages);
            rviMessages.scrollToPosition(it.size -1);
            if( it.isNotEmpty() &&  it.last().senderId != userId) {
                if(it.last().type == "TEXT") {
                    ChatManager.instance.getUserById(it.last().senderId) { user ->
                        sendNotification("1", user.username, it.last().text)
                    }
                }else {
                    ChatManager.instance.getUserById(it.last().senderId) { user ->
                        sendNotification("1", user.username, "Imagen")
                    }
                }

            }

            rviMessages.adapter = ChatListAdapter(
                userId,
                this,
                it
            )
        }

    }

    private fun createNotificationChannel(channelId: String) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Other User"
            val lastMessage = "Last Message"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelId, name, importance)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
            notificationManager.createNotificationChannel(channel)
        }

    }
    private fun sendNotification(channelId: String, username : String, textMessage : String) {
        val intent = Intent(this, ChannelActivity::class.java).apply{
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK;
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_user)
            .setContentTitle(username)
            .setContentText(textMessage)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        with(NotificationManagerCompat.from(this)) {
            notify(101, builder.build())
        }

    }

    fun getImage () {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(Intent.createChooser(gallery, "Select Image"), 2)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK &&
            data != null && data.data != null) {
            val img = data.data
            val userId = getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE).getString("LOGIN_ID", "")!!;
            var dataChannel = intent.extras;
            var channel = dataChannel!!.getLong("id").toString()
            ChatManager.instance.addImageToChannel(channel, userId, "IMAGE", img!! )

        }
    }

}