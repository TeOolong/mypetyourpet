package com.example.mypetyourpet.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mypetyourpet.R
import com.example.mypetyourpet.adapter.RequestsListAdapter
import com.example.mypetyourpet.model.Post
import com.example.mypetyourpet.model.PostsManager
import com.example.mypetyourpet.model.Request
import com.example.mypetyourpet.model.RequestsManager


class RequestsFragment(private val userId: String) : Fragment() {

    interface OnRequestListener {
        fun onSelectRequest(post: Post)
    }
    private var listener : OnRequestListener? = null;



    override fun onAttach(context: Context) {
        super.onAttach(context);
        listener = context as OnRequestListener;
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_requests, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cargarRequests(view)
    }
    override fun onResume() {
        super.onResume()
        cargarRequests(requireView());
    }

    fun cargarRequests(view: View) {

         RequestsManager.instance.getRequestByRequesterId(userId) { vgList: List<Request> ->
            //Toast.makeText(requireContext(), it.postPath.substringAfter("posts/").replace(" ",""), Toast.LENGTH_LONG).show();

             PostsManager.instance.getPostsByRequests(vgList) { posts ->
                 val rviRequests = view.findViewById<RecyclerView>(R.id.rviRequests)
                rviRequests.adapter = RequestsListAdapter(
                    posts,
                    this,
                    {
                        if (it.state == "pending") {
                            RequestsManager.instance.getRequestByPost(it.userId, it.id) {
                                RequestsManager.instance.deleteRequest(it) {
                                    cargarRequests(view)
                                };

                            }

                        } else if (it.state == "finished") {
                            RequestsManager.instance.getRequestByPost(it.userId, it.id) {
                                RequestsManager.instance.endRequest(it) {
                                    cargarRequests(view)
                                }

                            }

                        }



                    }, {
                        listener?.onSelectRequest(it)
                    })
            }


        }

    }
}