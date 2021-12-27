package com.example.mypetyourpet.model

import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.text.SimpleDateFormat
import java.util.*

class ChatManager {
    private val dbFirebase = Firebase.firestore;
    private val dbFirebaseImage = Firebase.storage;

    companion object {
        var instance = ChatManager()
            private set
    }

    fun createChannel(id : String, currentUserId : String, otherUserId : String, callbackOK : (Boolean) -> Unit) {
        dbFirebase.collection("channels")
            .whereEqualTo("users", arrayListOf(currentUserId, otherUserId))
            .get()
            .addOnSuccessListener { resA ->
                dbFirebase.collection("channels")
                    .whereEqualTo("users", arrayListOf(otherUserId, currentUserId))
                    .get()
                    .addOnSuccessListener { resB ->
                        if (resA.isEmpty && resB.isEmpty) {

                            dbFirebase.collection("channels").document(id).set(
                                hashMapOf(
                                    "users" to arrayListOf<String>(currentUserId, otherUserId),
                                )
                            )
                            callbackOK(false)

                        }else{
                            callbackOK(true)
                        }
                    }
            }
    }

    fun getChannelByUsers(currentUserId : String, otherUserId : String, callbackOK : (Channel) -> Unit) {
        dbFirebase.collection("channels")
            .whereEqualTo("users", arrayListOf(currentUserId, otherUserId))
            .get()
            .addOnSuccessListener { resA ->
                if (resA.isEmpty) {
                    dbFirebase.collection("channels")
                        .whereEqualTo("users", arrayListOf(otherUserId, currentUserId))
                        .get()
                        .addOnSuccessListener { resB ->
                            if (resB.isEmpty) {


                            } else {
                                val channels = arrayListOf<Channel>();
                                for (document in resB) {
                                    val req = Channel(
                                        document.id.toLong(),
                                        document.data["users"]!! as List<String>
                                    )
                                    channels.add(req);

                                }
                                callbackOK(channels[0])
                            }
                        }
                } else {
                    val channels = arrayListOf<Channel>();
                    for (document in resA) {
                        val req = Channel(
                            document.id.toLong(),
                            document.data["users"]!! as List<String>
                        )
                        channels.add(req);

                    }
                    callbackOK(channels[0])
                }

            }
    }

    fun getMessagesFromChannel(id : String, callbackOK: (List<TextMessage>) -> Unit){
        dbFirebase.collection("channels").document(id)
            .collection("messages")
            .get()
            .addOnSuccessListener {
                val textMessages = arrayListOf<TextMessage>();
                for (document in it) {
                    val textMessage = TextMessage(
                        document.data["text"]!! as String,
                        (document.data["time"]!! as Timestamp).toDate(),
                        document.data["senderId"]!! as String,
                        document.data["type"]!! as String

                    )
                    textMessages.add(textMessage);

                }
                callbackOK(textMessages)
            }
    }

    fun addListenerMessageToChannel(id : String, callbackOK: (List<TextMessage>) -> Unit) {
        dbFirebase.collection("channels").document(id)
            .collection("messages")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    return@addSnapshotListener
                }
                querySnapshot?.let {
                    val textMessages = arrayListOf<TextMessage>();
                    for (document in it) {
                        val textMessage = TextMessage(
                            document.data["text"]!! as String,
                            (document.data["time"]!! as Timestamp).toDate(),
                            document.data["senderId"]!! as String,
                            document.data["type"]!! as String

                        )
                        textMessages.add(textMessage);

                    }
                    callbackOK(textMessages)
                }
            }
    }
    fun addMessageToChannel(id : String, text : String, senderId : String, type : String) {
        val messageId = System.currentTimeMillis();
        val date = Calendar.getInstance().time
        dbFirebase.collection("channels").document(id)
            .collection("messages")
            .document(messageId.toString())
            .set(
                hashMapOf(
                    "text" to text,
                    "time" to date,
                    "senderId" to senderId,
                    "type" to type
                )
            )
    }

    fun addImageToChannel(id : String, senderId : String, type : String, uri: Uri) {
        val messageId = System.currentTimeMillis();
        val date = Calendar.getInstance().time
        val storageRef = dbFirebaseImage.reference.child("channels/$id/messages/${messageId}")
        storageRef.putFile(uri).addOnSuccessListener {
            val imgRef = dbFirebaseImage.reference.child("channels/$id/messages/${messageId}")
            imgRef.downloadUrl.addOnCompleteListener{
                var downloadUrl=it.getResult().toString();
                dbFirebase.collection("channels").document(id)
                    .collection("messages")
                    .document(messageId.toString())
                    .set(
                        hashMapOf(
                            "text" to downloadUrl,
                            "time" to date,
                            "senderId" to senderId,
                            "type" to type
                        )
                    )
            }
        }
    }
    fun getChannelsByUser(idUser: String, callbackOK : (List<Channel>) -> Unit) {
        dbFirebase.collection("channels")
            .whereArrayContainsAny("users", arrayListOf(idUser))
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                firebaseFirestoreException?.let {
                    return@addSnapshotListener
                }
                querySnapshot?.let {
                    val channels = arrayListOf<Channel>();
                    for (document in it) {
                        val req = Channel(document.id.toLong(),
                            document.data["users"]!! as List<String>
                        )
                        channels.add(req);

                    }
                    callbackOK(channels)
                }
            }
    }

    fun getUsersFromChannels(userId: String, channels : List<Channel>, callbackOK : (List<User>) -> Unit) {
        var usersIds = listOf<String>("0")
        if(channels.isNotEmpty()){
            usersIds = channels.map { channel ->
                channel.users.filter {
                    it != userId
                }[0]
            }
        }


        dbFirebase.collection("users")
            .whereIn(FieldPath.documentId(), usersIds)
            .get()
            .addOnSuccessListener {
                val users = arrayListOf<User>();
                for (document in it) {
                    val user = User(
                        document.id.toLong(),
                        document.data["username"]!! as String,
                        document.data["celular"]!! as Long
                    )
                    users.add(user);
                }
                callbackOK(users)
            }
    }

    fun getUserById(userId: String, callbackOK : (User) -> Unit){
        dbFirebase.collection("users")
            .whereEqualTo(FieldPath.documentId(), userId)
            .get()
            .addOnSuccessListener {
                val users = arrayListOf<User>();
                for (document in it) {
                    val user = User(
                        document.id.toLong(),
                        document.data["username"]!! as String,
                        document.data["celular"]!! as Long
                    )
                    users.add(user);
                }
                callbackOK(users[0])
            }
    }
}