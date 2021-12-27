package com.example.mypetyourpet.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mypetyourpet.R
import com.example.mypetyourpet.adapter.ChannelListAdapter
import com.example.mypetyourpet.model.Channel
import com.example.mypetyourpet.model.ChatManager
import com.example.mypetyourpet.model.User

class ChatFragment (private val userId: String) : Fragment() {

    interface OnChannelSelectedListener {
        fun onSelectChannel(channel: Channel)
    }
    private var listener : OnChannelSelectedListener? = null;

    override fun onAttach(context: Context) {
        super.onAttach(context);
        listener = context as OnChannelSelectedListener;
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ChatManager.instance.getChannelsByUser(userId) { vgList: List<Channel> ->
            ChatManager.instance.getUsersFromChannels(userId,vgList) {
                val rviChannels = view.findViewById<RecyclerView>(R.id.rviChannels)
                //rviPosts.adapter = null;
                rviChannels.adapter = ChannelListAdapter(
                    it,
                    this
                ) { user: User ->
                    ChatManager.instance.getChannelByUsers(userId, user.id.toString()){ channel ->
                        listener?.onSelectChannel(channel)
                    }

                }
            }

        }
    }
}