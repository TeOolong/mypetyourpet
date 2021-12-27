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
import com.example.mypetyourpet.model.Post

class CatalogListAdapter (private val postList : List<Post>,
                          private val fragment : Fragment,
                          private val listener : (Post) -> Unit) :
    RecyclerView.Adapter<CatalogListAdapter.ViewHolder>() {
    class ViewHolder(
        view: View, val listener: (Post) -> Unit,
        val postList: List<Post>
    ) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        val iviPet : ImageView
        val tviName : TextView
        val tviSpecies : TextView



        init {
            iviPet = view.findViewById(R.id.iviPhoto)
            tviName = view.findViewById(R.id.tviName)
            tviSpecies = view.findViewById(R.id.tviSpecies)


            view.setOnClickListener(this);
        }

        override fun onClick(p0: View?) {
            listener(postList[adapterPosition])
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_catalog, parent, false)
        val viewHolder = ViewHolder(view, listener,postList)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tviName.text = postList[position].name;
        holder.tviSpecies.text = postList[position].species;
        Glide.with(fragment).load(postList[position].image).into(holder.iviPet)
    }

    override fun getItemCount(): Int {
        return postList.size;
    }
}