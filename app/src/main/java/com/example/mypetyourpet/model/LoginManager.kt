package com.example.mypetyourpet.model

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginManager {
    companion object {
        var instance = LoginManager()
            private set
    }


    private val dbFirebase = Firebase.firestore;
    fun saveUser(username: String, password : String, celular : String) {
        val phone = "51"+celular;
        val data = hashMapOf<String, Any>(
            "username" to username,
            "celular" to phone.toLong(),
            "password" to password
        )
        val userId = System.currentTimeMillis();
        dbFirebase.collection("users").document(
            userId.toString()
        ).set(data)
    }

    fun checkRegister(username: String, password : String, celular : Long, callbackOK: (String) -> Unit) {
        var resp = ""
        if(username != "" && password!= "" && celular.toString().length== 9) {
            dbFirebase.collection("users")
                .get()
                .addOnSuccessListener { res ->
                    for (names in res) {
                        if (names.data["username"]!!.toString() == username)
                            resp = "Usuario ya ha sido registrado";
                        else {
                            if((names.data["celular"]!! as Long) == celular){
                                resp = "Este celular ya ha sido registrado";
                            }
                        }
                    }
                    callbackOK(resp);
                }
        }
        else {
            resp = "Llenar correctamente todos los campos"
            callbackOK(resp);
        }



    }

    fun checkLogin(username: String, password: String, callbackOK: (String,String) -> Unit) {
        var resp = "usuario no existe"
        var id=""
        if(username != "" && password!= "") {
            dbFirebase.collection("users")
                .get()
                .addOnSuccessListener { res ->
                    for (names in res) {
                        if (names.data["username"]!!.toString() == username && names.data["password"]!!.toString() != password)
                            resp = "Contraseñas no coinciden";
                        else if(names.data["username"]!!.toString() == username && names.data["password"]!!.toString() == password){
                            resp = ""
                            id = names.id
                        }
                    }
                    callbackOK(resp,id);

                }
        }
        else {
            resp = "Llenar correctamente todos los campos"
            callbackOK(resp,id);
        }

    }
}