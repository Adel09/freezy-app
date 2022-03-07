package com.crumlabs.freezyapp

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.crumlabs.freezyapp.models.Item
import com.crumlabs.freezyapp.models.User
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {


    lateinit var database: DatabaseReference


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
        val query: DatabaseReference? = database.child("items").child(itemid!!)

        if (query != null) {
            query.get().addOnSuccessListener {
                val item = it.getValue<Item>()
                println(item?.name)

                    if (item?.name != null) {



                        item_detail_name.text = item.name
                        item_detail_location.text = "${item.city} \uD83D\uDCCD"
                        item_detail_description.text = item.description
                        Picasso.get().load(item.imgUrl).placeholder(R.drawable.placeholder).into(item_img_detail)
                        println("The item itself is $item")
                        println("Item name: ${item.name}")
                        setOwner(item.owner!!)

                    }else{
                        println("The item itself is $item")
                        println("Item name is null")
                    }

            }
        }



    }

    fun setOwner(uid: String) {
        val ownerQuery: DatabaseReference? = database.child("users").child(uid)
        lateinit var owner: User
        if (ownerQuery != null) {
            ownerQuery.get().addOnSuccessListener {
                owner = it.getValue<User>()!!
                print(owner?.name)
                owner_name_detail.text = owner.name
                owner_city_detail.text = "Lives in ${owner.city}"

            }

        }else{
            println("Owner is null")
        }



    }

}