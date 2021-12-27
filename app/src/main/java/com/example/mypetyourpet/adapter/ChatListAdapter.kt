package com.example.mypetyourpet.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mypetyourpet.ChannelActivity
import com.example.mypetyourpet.R
import com.example.mypetyourpet.model.TextMessage
import java.text.SimpleDateFormat

class ChatListAdapter (private val userId :String,
                       private val activity: ChannelActivity,
                       private val userTextsMessage : List<TextMessage>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val typeText = 1;
    private val typeImage = 2;

    class TextViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view)
        {
        val rlMessage : RelativeLayout
        val tviTextMessage : TextView
        val tviMessageTime : TextView

        init {
            rlMessage = view.findViewById(R.id.rlMessage)
            tviTextMessage = view.findViewById(R.id.tviTextMessage)
            tviMessageTime = view.findViewById(R.id.tviMessageTime)
        }

    }

    class ImageViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view)
    {
        val rlImgMessage : RelativeLayout
        val iviImageMessage : ImageView
        val tviMessageTime : TextView

        init {
            rlImgMessage = view.findViewById(R.id.rlImgMessage)
            iviImageMessage = view.findViewById(R.id.iviImageMessage)
            tviMessageTime = view.findViewById(R.id.tviMessageTime)
        }

    }

    override fun getItemViewType(position: Int): Int {
        if(userTextsMessage[position].type == "TEXT"){
            return typeText
        }else {
            return typeImage
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if(viewType == typeText) {
            val viewTextMessage = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
            return  TextViewHolder(viewTextMessage)
        }
        else{
            val viewImageMessage = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_msgimage, parent, false)
            return ImageViewHolder(viewImageMessage)
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(getItemViewType(position) == typeText){
            val dateFormat = SimpleDateFormat
                .getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)
            (holder as TextViewHolder).tviTextMessage.text = userTextsMessage[position].text;
            (holder as TextViewHolder).tviMessageTime.text = dateFormat.format(userTextsMessage[position].time);
            if(userTextsMessage[position].senderId!=userId) {
                (holder as TextViewHolder).tviTextMessage.textAlignment =
                    View.TEXT_ALIGNMENT_VIEW_START;
                (holder as TextViewHolder).tviMessageTime.textAlignment =
                    View.TEXT_ALIGNMENT_VIEW_START;
                (holder as TextViewHolder).rlMessage.setBackgroundResource(R.drawable.message_other_shape)
                (holder as TextViewHolder).rlMessage.apply {
                    val params = FrameLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        Gravity.START
                    )
                    this.layoutParams = params;
                }
            }

        }else {
            val dateFormat = SimpleDateFormat
                .getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)
            (holder as ImageViewHolder).tviMessageTime.text = dateFormat.format(userTextsMessage[position].time);
            Glide.with(activity).load(userTextsMessage[position].text).into((holder as ImageViewHolder).iviImageMessage);
            if(userTextsMessage[position].senderId!=userId) {
                (holder as ImageViewHolder).tviMessageTime.textAlignment =
                    View.TEXT_ALIGNMENT_VIEW_START;
                (holder as ImageViewHolder).rlImgMessage.setBackgroundResource(R.drawable.message_other_shape)
                (holder as ImageViewHolder).rlImgMessage.apply {
                    val params = FrameLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        Gravity.START
                    )
                    this.layoutParams = params;
                }
            }
        }


    }



    override fun getItemCount(): Int {
        return userTextsMessage.size;
    }
}