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

class RequestsListAdapter (private val postList : List<Post>,
                           private val fragment : Fragment,
                           private val listenerClose : (Post) -> Unit,
                           private val listenerRequest : (Post) -> Unit):
    RecyclerView.Adapter<RequestsListAdapter.ViewHolder>() {
    class ViewHolder(
        view: View, val listenerRequest: (Post) -> Unit,
        val postList: List<Post>
    ) : RecyclerView.ViewHolder(view),View.OnClickListener{

        val iviPet : ImageView
        val tviName : TextView
        val butClose : Button
        var tviState : TextView

        init {
            iviPet = view.findViewById(R.id.iviPhoto)
            tviName = view.findViewById(R.id.tviName)
            butClose = view.findViewById(R.id.butClose)
            tviState = view.findViewById(R.id.tviState);
            view.setOnClickListener(this);
        }
        override fun onClick(p0: View?) {
            listenerRequest(postList[adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_request, parent, false)
        val viewHolder = ViewHolder(view, listenerRequest, postList)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tviName.text = postList[position].name;
        Glide.with(fragment).load(postList[position].image).into(holder.iviPet);
        holder.butClose.setOnClickListener{ listenerClose(postList[position])}

        if(postList[position].state == "pending") {
            holder.tviState.text = "Estado: En progreso"
        }
        else if (postList[position].state == "finished") {
            holder.tviState.text = "Estado: Aceptado"
        }

    }

    override fun getItemCount(): Int {
        return postList.size;
    }
}