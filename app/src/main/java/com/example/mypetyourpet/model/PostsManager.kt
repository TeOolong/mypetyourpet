package com.example.mypetyourpet.model

import android.net.Uri
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class PostsManager() {
    private  val dbFirebase = Firebase.firestore;
    private  val dbFirebaseImage = Firebase.storage;
    companion object {
        var instance = PostsManager()
            private set
    }

    fun addPost(id : String, name : String, species : String, description : String, uri: Uri) {
        val postId = System.currentTimeMillis();

        val storageRef = dbFirebaseImage.reference.child("users/$id/posts/${postId}")
        storageRef.putFile(uri).addOnSuccessListener {
            val imgRef = dbFirebaseImage.reference.child("users/$id/posts/${postId}")
            imgRef.downloadUrl.addOnCompleteListener{
                var downloadUrl=it.getResult().toString();
                dbFirebase.collection("users")
                    .document(id)
                    .collection("posts")
                    .document(postId.toString())
                    .set(
                        hashMapOf(
                            "name" to name,
                            "species" to species,
                            "description" to description,
                            "image" to downloadUrl,
                            "state" to "free",
                            "userId" to id
                        )
                    )
            }
        }
    }

    fun deletePost(post: Post) {
        val storageRef = dbFirebaseImage.reference.child("users/${post.userId}/posts/${post.id}")
        storageRef.delete()
        dbFirebase.collection("users").document(post.userId).collection("posts")
            .document(post.id.toString()).delete();

    }

    fun convertToRequest(id : String, post: Post) {
        dbFirebase.collection("users").document(post.userId).collection("posts")
            .document(post.id.toString()).update("state","pending");

        val requestId = System.currentTimeMillis();
        dbFirebase.collection("requests")
            .document(requestId.toString())
            .set(
                hashMapOf(
                    "publisherId" to post.userId,
                    "requesterId" to id,
                    "postPath" to "users/${post.userId}/posts/${post.id}"
                )
            )
    }

    fun getAllUserPosts(id : String, callbackOK : (List<Post>) -> Unit) {
        dbFirebase.collection("users")
            .document(id)
            .collection("posts")
            .orderBy("name")
            .get()
            .addOnSuccessListener {
                val Posts = arrayListOf<Post>();
                for (document in it) {
                    val post = Post(document.id.toLong(),
                        document.data["name"]!! as String,
                        document.data["species"]!! as String,
                        document.data["description"]!! as String,
                        document.data["image"]!! as String,
                        document.data["state"]!! as String,
                        document.data["userId"]!! as String
                    )
                    Posts.add(post);
                }
                callbackOK(Posts);
            }

    }

    fun getPostsByRequests( requests : List<Request>, callbackOK: (List<Post>) -> Unit ){
        var postsId = arrayListOf<String>()
        requests.forEach {
            postsId.add(it.postPath.substringAfter("posts/").replace(" ","") )
        }
        dbFirebase.collectionGroup("posts")
            .get()
            .addOnSuccessListener {

                    val Posts = arrayListOf<Post>();

                    for (document in it) {
                        if (document.id in postsId) {
                            val post = Post(
                                document.id.toLong(),
                                document.data["name"]!! as String,
                                document.data["species"]!! as String,
                                document.data["description"]!! as String,
                                document.data["image"]!! as String,
                                document.data["state"]!! as String,
                                document.data["userId"]!! as String
                            )
                            Posts.add(post);
                        }

                    }
                    callbackOK(Posts);
                }


    }

    fun getAllPostsFiltered(id : String, searchText : String, callbackOK : (List<Post>) -> Unit) {
        dbFirebase.collection("users")
            .document(id)
            .collection("posts")
            .get()
            .addOnSuccessListener { res ->
                var postsUser = arrayListOf<String>();
                for (ref in res) {
                    postsUser.add(ref.id)
                }
                if (postsUser.isEmpty()) {
                    postsUser.add("0");
                }
                dbFirebase.collectionGroup("posts")
                    //.whereEqualTo("species", "vaso")
                    .get()
                    .addOnSuccessListener {
                        val Posts = arrayListOf<Post>();

                        for (document in it) {
                            if(document.id !in postsUser  && (document.data["species"]!! as String) == searchText && (document.data["state"]!! as String)=="free") {
                                val post = Post(document.id.toLong(),
                                    document.data["name"]!! as String,
                                    document.data["species"]!! as String,
                                    document.data["description"]!! as String,
                                    document.data["image"]!! as String,
                                    document.data["state"]!! as String,
                                    document.data["userId"]!! as String
                                )
                                Posts.add(post);
                            }

                        }

                        callbackOK(Posts);
                    }

            }
    }
    fun getAllPosts(id : String, callbackOK : (List<Post>) -> Unit) {

        dbFirebase.collection("users")
            .document(id)
            .collection("posts")
            .get()
            .addOnSuccessListener { res ->
                var postsUser = arrayListOf<String>();
                for (ref in res) {
                    postsUser.add(ref.id)
                }
                if (postsUser.isEmpty()) {
                    postsUser.add("0");
                }
                dbFirebase.collectionGroup("posts")
                    .get()
                    .addOnSuccessListener {
                        val Posts = arrayListOf<Post>();

                        for (document in it) {
                            if(document.id !in postsUser && (document.data["state"]!! as String)=="free") {
                                val post = Post(document.id.toLong(),
                                    document.data["name"]!! as String,
                                    document.data["species"]!! as String,
                                    document.data["description"]!! as String,
                                    document.data["image"]!! as String,
                                    document.data["state"]!! as String,
                                    document.data["userId"]!! as String
                                )
                                Posts.add(post);
                            }

                        }
                        callbackOK(Posts);
                    }

            }



        }



    }



