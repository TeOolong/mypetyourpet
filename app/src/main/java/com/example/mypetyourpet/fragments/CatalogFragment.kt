package com.example.mypetyourpet.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mypetyourpet.R
import com.example.mypetyourpet.adapter.CatalogListAdapter
import com.example.mypetyourpet.model.Post
import com.example.mypetyourpet.model.PostsManager

class CatalogFragment(private val userId: String) : Fragment() {

    interface OnPostSelectedListener {
        fun onSelectInCatalog(post: Post)
    }
    private var listener : OnPostSelectedListener? = null;

    override fun onAttach(context: Context) {
        super.onAttach(context);
        listener = context as OnPostSelectedListener;
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_catalog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var textChange = false;
        cargarPosts(view)
        val searchView = view.findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                val searchText = searchView.query.toString().replace(" ","") ;
                cargarPostsFiltrados(view,searchText)
                textChange = true
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if(textChange) {

                    cargarPosts(view)
                    textChange = false
                }
                return true
            }

        })

    }
    override fun onResume() {
        super.onResume()
        cargarPosts(requireView());
    }

    fun cargarPosts(view: View) {
        PostsManager.instance.getAllPosts(userId) { vgList: List<Post> ->

            val rviPosts = view.findViewById<RecyclerView>(R.id.rviPosts)
            //rviPosts.adapter = null;
            rviPosts.adapter = CatalogListAdapter(
                vgList,
                this
            ) { post: Post ->
                listener?.onSelectInCatalog(post)
            }
        }
    }

    fun cargarPostsFiltrados(view: View, searchText : String) {
        PostsManager.instance.getAllPostsFiltered(userId,searchText) { vgList: List<Post> ->
            val rviPosts = view.findViewById<RecyclerView>(R.id.rviPosts)
            //rviPosts.adapter = null;
            rviPosts.adapter = CatalogListAdapter(
                vgList,
                this
            ) { post: Post ->
                listener?.onSelectInCatalog(post)
            }
        }
    }
}