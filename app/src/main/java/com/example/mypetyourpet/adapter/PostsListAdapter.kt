package com.example.mypetyourpet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mypetyourpet.R
import com.example.mypetyourpet.model.Post

class PostsListAdapter (private val postList : List<Post>,
                        private val fragment : Fragment,
                        private val listenerClose : (Post) -> Unit,
                        private val listenerSolicitud : (Post) -> Unit):
    RecyclerView.Adapter<PostsListAdapter.ViewHolder>() {
    class ViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view){

        val iviPet : ImageView
        val tviName : TextView
        val butSolicitud : Button
        val butClose : Button


        init {
            iviPet = view.findViewById(R.id.iviPhoto)
            tviName = view.findViewById(R.id.tviName)
            butSolicitud = view.findViewById(R.id.butSolicitud)
            butClose = view.findViewById(R.id.butClose)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tviName.text = postList[position].name;
        Glide.with(fragment).load(postList[position].image).into(holder.iviPet);
        holder.butSolicitud.setOnClickListener{ listenerSolicitud(postList[position])}
        holder.butClose.setOnClickListener{ listenerClose(postList[position])}
        if(postList[position].state == "free") {
            holder.butSolicitud.setVisibility(View.INVISIBLE)
        }
        else if(postList[position].state == "pending"){
            holder.butSolicitud.setVisibility(View.VISIBLE)
        }
        else if (postList[position].state == "finished") {
            holder.butSolicitud.setVisibility(View.VISIBLE)
            holder.butSolicitud.text = "Ver datos"
        }
    }

    override fun getItemCount(): Int {
        return postList.size;
    }
}
