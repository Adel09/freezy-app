package com.crumlabs.freezyapp

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.crumlabs.freezyapp.models.Item
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {


    lateinit var database: DatabaseReference
    lateinit var itemv: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val extras: Bundle? = intent.extras
        val itemid: String? = extras?.getString("itemid").toString()
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
        println("Id received is $itemid")
        database = Firebase.database.reference
        val query: Query? = database.child("items").orderByChild("id").equalTo(itemid)

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Item object and use the values to update the UI

                for (item in dataSnapshot.children){

                    itemv = item.getValue<Item>()!!

                    if (itemv?.name != null) {
                        item_detail_name.text = itemv.name
                        item_detail_location.text = "${itemv.city} \uD83D\uDCCD"
                        item_detail_description.text = itemv.description
                        Picasso.get().load(itemv.imgUrl).placeholder(R.drawable.placeholder).into(item_img_detail)
                        println("The item itself is $itemv")
                        println("Item name: ${itemv.name}")
                        getAndSetOwner(itemv)
                    }else{
                        println("The item itself is $itemv")
                        println("Item name is null")
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        if (query != null) {
            //query.addValueEventListener(postListener)
            query.addListenerForSingleValueEvent(postListener)
        }else{
            println("Query is null")
        }

    }

    fun getAndSetOwner(item: Item){
        if (item != null){
            val query: Query? = database.child("users").child(item.owner!!)
            if (query != null) {
                query.get().addOnSuccessListener {
                    println("The owner is ${it.value}")
                }.addOnFailureListener {
                    println("There was a problem getting the owner")
                    println(it.message)
                }
            }
        }

    }

}