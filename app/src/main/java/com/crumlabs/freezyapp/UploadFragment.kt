package com.crumlabs.freezyapp

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.crumlabs.freezyapp.models.Item
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.lang.Exception
import java.util.*


class UploadFragment : Fragment() {

    lateinit var imageView: ImageView
    lateinit var itemName: TextInputEditText
    lateinit var itemDescription: TextInputEditText
    lateinit var button: Button
    lateinit var publishButton: Button
    lateinit var progressBar: ProgressBar

    private val pickImage = 100
    private var imageUri: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private lateinit var database: DatabaseReference
    lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_upload, container, false)
        itemName = view.findViewById(R.id.item_name)
        itemDescription = view.findViewById(R.id.item_description)
        imageView = view.findViewById(R.id.itemImgView)
        button = view.findViewById(R.id.button2)
        progressBar = view.findViewById(R.id.progressBar2)
        button.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
        database = Firebase.database.reference
        firebaseStore = FirebaseStorage.getInstance()
        user = Firebase.auth.currentUser!!
        storageReference = FirebaseStorage.getInstance().reference
        publishButton = view.findViewById(R.id.publish_button)
        publishButton.setOnClickListener {
            Toast.makeText(context, "Please wait", Toast.LENGTH_LONG).show()
            progressBar.visibility = View.VISIBLE
            uploadImage()
            //var city = "Nothing"

            println("Successful")
        }


        return view

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
            println(imageUri)
        }
    }



    private fun uploadImage(){
        if(imageUri != null){
            println("Starting upload")
            Toast.makeText(context, "Please Wait", Toast.LENGTH_SHORT).show()
            val ref = storageReference?.child("uploads/items/${itemName.text.toString()}/" + UUID.randomUUID().toString())
            println("Reference made")
            val uploadTask = ref?.putFile(imageUri!!)
            println("File put")

            val urlTask = uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            })?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    val downloadurl = downloadUri.toString()
                    println(downloadurl)

                    database.child("users").child(user!!.uid).child("city").get().addOnSuccessListener {
                        Log.i("firebase", "Got value ${it.value}")
                        val id: String = UUID.randomUUID().toString()
                        val name = itemName.text.toString()
                        val desc = itemDescription.text.toString()
                        //city = it.value.toString()
                        println(downloadurl)
                        try {
                            if (!name.isEmpty()) {
                                val item = Item(id = id, name = name, description = desc, imgUrl = downloadurl, city = it.value.toString(), owner = user.uid)
                                database.child("items").child(id).setValue(item)
                                itemName.setText("")
                                itemDescription.setText("")
                                Toast.makeText(context, "Thanks for posting", Toast.LENGTH_LONG).show()
                                progressBar.visibility = View.GONE
                                println("Sent to db")
                            }else{
                                Toast.makeText(context, "Please choose an item", Toast.LENGTH_LONG).show()
                            }

                        }catch (e: Exception){
                            progressBar.visibility = View.GONE
                            Toast.makeText(context, "There was a problem posting the item", Toast.LENGTH_LONG).show()
                            println(e.message)
                        }

                    }.addOnFailureListener{
                        Toast.makeText(context, "Error getting data", Toast.LENGTH_LONG).show()
                        Log.e("firebase", "Error getting data", it)
                    }

                //addUploadRecordToDb(downloadUri.toString())
                } else {
                    // Handle failures
                    println("Something dey sup ${task.exception}")
                }
            }?.addOnFailureListener{
                println("Something dey sup ")
            }
        }else{
            Toast.makeText(context, "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }

//    private fun addUploadRecordToDb(uri: String){
//        val db = Firebase.database.reference
//
//
//        val data = HashMap<String, Any>()
//        data["imageUrl"] = uri
//
//        db.collection("posts")
//                .add(data)
//                .addOnSuccessListener { documentReference ->
//                    Toast.makeText(this, "Saved to DB", Toast.LENGTH_LONG).show()
//                }
//                .addOnFailureListener { e ->
//                    Toast.makeText(this, "Error saving to DB", Toast.LENGTH_LONG).show()
//                }
//    }


}