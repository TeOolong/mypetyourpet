package com.example.mypetyourpet

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.mypetyourpet.model.PostsManager
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws

class PostActivity : AppCompatActivity() {
    private var photoUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        getSupportActionBar()?.hide();
        var eteName = findViewById<EditText>(R.id.eteName);
        var spType = findViewById<Spinner>(R.id.spType);
        var type : String = "";
        val list = arrayListOf<String>("Ingresar tipo","perro", "gato","conejo", "hamster", "otros");
        val adapter : ArrayAdapter<String> = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, list)
        spType.adapter = adapter;

        spType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                type = list[p2];
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        var eteDescription = findViewById<EditText>(R.id.eteDescription);

        var butTakePhoto = findViewById<ImageButton>(R.id.butTakePhoto)
        butTakePhoto.setOnClickListener {
            takePhoto()

        }

        val userId = getSharedPreferences("LOGIN_INFO", Context.MODE_PRIVATE).getString("LOGIN_ID", "")!!;
        var butPost = findViewById<Button>(R.id.butPost)
        butPost.setOnClickListener{
            if(type == "Ingresar tipo") {
                Toast.makeText(this, "Ingresar tipo de mascota", Toast.LENGTH_LONG).show();
            }else {
                PostsManager.instance.addPost(
                    userId,
                    eteName.text.toString(),
                    type,
                    eteDescription.text.toString(),
                    photoUri!!
                )
                onBackPressed()
            }

        }
        var butCancel = findViewById<Button>(R.id.butCancel)
        butCancel.setOnClickListener{
            onBackPressed()
        }
    }



    fun takePhoto() {

        var imageFile : File? = null
        try {
            imageFile = createImageFile()
        }catch (ioe : IOException) {
            Log.e("FotoActivity", "No se pudo crear archivo de imagen")
            return
        }
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoURI = FileProvider.getUriForFile(
            this,
            "com.example.mypetyourpet.fileprovider",
            imageFile
        )
        photoUri = photoURI;

        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        startActivityForResult(intent, 200)
    }

    @Throws(IOException::class)
    fun createImageFile() : File {
        val timestamp = SimpleDateFormat("yyyyMMddd_HHmmss").format(Date())
        val imageFile = File.createTempFile(
            "${timestamp}_",
            ".jpg",
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
        return imageFile
    }
    fun Bitmap.rotate(degrees: Float): Bitmap {
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == RESULT_OK) {
            var butTakePhoto = findViewById<ImageButton>(R.id.butTakePhoto)
            var yourBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, photoUri)
            val resized = Bitmap.createScaledBitmap(yourBitmap, 400, 300, true)
            val rotated = resized.rotate(90f)
            butTakePhoto.setImageBitmap(rotated);
        }
    }

}