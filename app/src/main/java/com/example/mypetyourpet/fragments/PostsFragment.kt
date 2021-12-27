package com.example.mypetyourpet.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mypetyourpet.PostActivity
import com.example.mypetyourpet.R
import com.example.mypetyourpet.adapter.PostsListAdapter
import com.example.mypetyourpet.model.Post
import com.example.mypetyourpet.model.PostsManager
import com.example.mypetyourpet.model.RequestsManager

class PostsFragment(private val userId: String) : Fragment() {
    interface OnPendingRequestListener {
        fun onSelectPendingRequest(post: Post)
    }
    private var listener : OnPendingRequestListener? = null;

    override fun onAttach(context: Context) {
        super.onAttach(context);
        listener = context as OnPendingRequestListener;
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_posts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val butCreatePost = view.findViewById<Button>(R.id.butCreatePost)
        butCreatePost.setOnClickListener {
            val intent : Intent = Intent ();
            intent.setClass(requireContext(), PostActivity::class.java)
            startActivity(intent);
        }

    }

    override fun onResume() {
        super.onResume()
        cargarPosts(requireView());
    }

    fun cargarPosts(view: View) {
        PostsManager.instance.getAllUserPosts(userId) { vgList: List<Post> ->

            val rviPosts = view.findViewById<RecyclerView>(R.id.rviPosts)
            rviPosts.adapter = PostsListAdapter(
                vgList,
                this,
                {
                    if(it.state == "free"){
                        PostsManager.instance.deletePost(it);
                        cargarPosts(view);
                    }
                    else if(it.state == "pending"){
                        Toast.makeText(requireContext(), "Posee una solicitud pendiente", Toast.LENGTH_LONG).show();
                    }
                    else {
                        RequestsManager.instance.getRequestByPost(it.userId,it.id) {
                            RequestsManager.instance.endRequest(it) {
                                cargarPosts(view);
                            }

                        }
                    }

                }, {
                    listener?.onSelectPendingRequest(it)
                }
            )

        }
    }

}