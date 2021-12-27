package com.example.mypetyourpet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mypetyourpet.R
import com.example.mypetyourpet.model.User

class ChannelListAdapter (private val userList : List<User>,
                          private val fragment : Fragment,
                          private val listener : (User) -> Unit):
    RecyclerView.Adapter<ChannelListAdapter.ViewHolder>() {
    class ViewHolder(
        view: View, val listener: (User) -> Unit,
        val userList: List<User>
    ) : RecyclerView.ViewHolder(view),
        View.OnClickListener{

        val iviPhoto : ImageView
        val tviUsername : TextView
        val tviPhone : TextView

        init {
            iviPhoto = view.findViewById(R.id.iviPhoto)
            tviUsername = view.findViewById(R.id.tviUsername)
            tviPhone = view.findViewById(R.id.tviPhone)
            view.setOnClickListener(this);
        }
        override fun onClick(p0: View?) {
            listener(userList[adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        val viewHolder = ViewHolder(view, listener, userList)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tviUsername.text = userList[position].username;
        holder.tviPhone.text = userList[position].phone.toString();
        Glide.with(fragment).load(R.drawable.ic_user).into(holder.iviPhoto);

    }

    override fun getItemCount(): Int {
        return userList.size;
    }
}
