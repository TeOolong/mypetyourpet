package com.example.mypetyourpet.model

import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class RequestsManager {
    private  val dbFirebase = Firebase.firestore;
    private  val dbFirebaseImage = Firebase.storage;
    companion object {
        var instance = RequestsManager()
            private set
    }

    fun endRequest(request: Request, callbackOK: (Boolean) -> Unit) {
        val storageRef = dbFirebaseImage.reference.child(request.postPath)
        storageRef.delete()
        dbFirebase.collection("requests")
            .document(request.id.toString())
            .delete()
        dbFirebase.document(request.postPath).delete().addOnSuccessListener {
            callbackOK(true)
        }

    }

    fun deleteRequest(request: Request, callbackOK: (Boolean) -> Unit) {
        dbFirebase.collection("requests")
            .document(request.id.toString())
            .delete()
        dbFirebase.document(request.postPath).update("state","free").addOnSuccessListener {
            callbackOK(true)
        }

    }

    fun acceptRequest(request: Request, callbackOK: (Boolean) -> Unit) {
        dbFirebase.document(request.postPath).update("state","finished").addOnSuccessListener {
            callbackOK(true)
        }

    }

    fun getRequestByRequesterId(reqId : String , callbackOK : (List<Request>) -> Unit) {
        dbFirebase.collection("requests")
            .whereEqualTo("requesterId", reqId)
            .get()
            .addOnSuccessListener {
                val requests = arrayListOf<Request>();
                for (document in it) {
                    val req = Request(document.id.toLong(),
                        document.data["requesterId"]!! as String,
                        document.data["publisherId"]!! as String,
                        document.data["postPath"]!! as String
                    )
                    requests.add(req);

                }
                callbackOK(requests)
            }
    }


    fun canMakeARequest(reqId : String , callbackOK : (Boolean) -> Unit) {
        dbFirebase.collection("requests")
            .whereEqualTo("requesterId", reqId)
            .get()
            .addOnSuccessListener {
                val requests = arrayListOf<Request>();
                if(it.size()<=3){
                    callbackOK(true)
                }
                callbackOK(false)
            }
    }


    fun getRequesterData(reqId : String, callbackOK : (User) -> Unit) {
        dbFirebase.collection("users")
            .whereEqualTo(FieldPath.documentId(), reqId)
            .get()
            .addOnSuccessListener {
                val users = arrayListOf<User>();
                for (document in it) {
                    val req = User(document.id.toLong(),
                        document.data["username"]!! as String,
                        document.data["celular"]!! as Long
                    )
                    users.add(req);

                }
                callbackOK(users[0])
            }
    }

    fun getPublisherData(postId : String, callbackOK : (User) -> Unit) {
        dbFirebase.collection("users")
            .whereEqualTo(FieldPath.documentId(), postId)
            .get()
            .addOnSuccessListener {
                val users = arrayListOf<User>();
                for (document in it) {
                    val req = User(document.id.toLong(),
                        document.data["username"]!! as String,
                        document.data["celular"]!! as Long
                    )
                    users.add(req);

                }
                callbackOK(users[0])
            }
    }
    fun getRequestByPost(userId : String, postId: Long, callbackOK: (Request) -> Unit) {
        dbFirebase.collection("requests")
            .whereEqualTo("postPath", "users/${userId}/posts/${postId}")
            .get()
            .addOnSuccessListener {
                val requests = arrayListOf<Request>();
                for (document in it) {
                    var req = Request(
                        document.id.toLong(),
                        document.data["requesterId"]!! as String,
                        document.data["publisherId"]!! as String,
                        document.data["postPath"]!! as String
                    )
                    requests.add(req);

                }
                callbackOK(requests[0])

            }
    }

}