package com.crumlabs.freezyapp

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ProgressBar
import android.widget.Toast
import com.crumlabs.freezyapp.adapter.MyCustomAdapter
import com.crumlabs.freezyapp.models.Item
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_explore.*
import java.lang.Exception
import java.util.ArrayList


class ExploreFragment : Fragment() {
    private lateinit var database: DatabaseReference
    //var items: MutableList<Item>? = null
    //var items: MutableList<Item?>? = mutableListOf()

    lateinit var adapter: MyCustomAdapter
    lateinit var gridView: GridView
    lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("first")
        database = Firebase.database.reference
        val query: Query = database.child("items")

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                var items: ArrayList<Item?>? = arrayListOf()
                try {

                    for (item in dataSnapshot.children) {
                        val itemv: Item? = item.getValue<Item>()
                        println("${itemv?.name}")
                        println(itemv?.description)
                        println(itemv.toString())
                        items?.add(itemv)
                        println("Items from onCreate: $items")
                    }

                    println("Should be last")
                    adapter = MyCustomAdapter(items!!)
                    if(items?.size != 0){
                        println("Adapter")
                        gridView.adapter = adapter
                        progressBar.visibility = View.GONE

                        println(items)
                    }else{
                        Toast.makeText(context, "No items found", Toast.LENGTH_LONG).show()
                    }



                }catch (e: Exception){
                    println(e.message)
                    Toast.makeText(context, "Problem retrieving data", Toast.LENGTH_LONG).show()
                    println("The problem is with retrieval")
                }

            }


            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        query.addValueEventListener(postListener)


    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        println("second")

        gridView = view.findViewById<GridView>(R.id.gridview)
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        Toast.makeText(context, "Loading items", Toast.LENGTH_SHORT).show()






        println("Got to here")




        return view
    }

}