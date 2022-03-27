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
import com.crumlabs.freezyapp.components.ExpandableHeightGridView
import com.crumlabs.freezyapp.models.Item
import com.crumlabs.freezyapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.fragment_explore.*
import java.lang.Exception
import java.util.ArrayList


class ExploreFragment : Fragment() {
    private lateinit var database: DatabaseReference
    //var items: MutableList<Item>? = null
    //var items: MutableList<Item?>? = mutableListOf()

    lateinit var adapter: MyCustomAdapter
    lateinit var gridView: ExpandableHeightGridView
    private lateinit var auth: FirebaseAuth
    lateinit var user: FirebaseUser
    lateinit var progressBar: ProgressBar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = arguments
        val city = args?.getString(USER_ADDRESS)

        auth = Firebase.auth
        user = auth.currentUser!!
        println("first")
        database = Firebase.database.reference



        val query: Query = database.child("items").orderByChild("city").equalTo(city)
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

        gridView = view.findViewById<ExpandableHeightGridView>(R.id.gridView)
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        Toast.makeText(context, "Loading items", Toast.LENGTH_SHORT).show()


        println("Got to here")



        return view
    }


//     fun getCity(uid: String) {
//        println("getCity called")
//        val ownerQuery: DatabaseReference? = database.child("users").child(uid)
//        lateinit var owner: User
//        if (ownerQuery != null) {
//            println("Owner query not null")
//            ownerQuery.get().addOnSuccessListener {
//                println("reached onsuccesslistener")
//                owner = it.getValue<User>()!!
//                print(owner?.name)
//                this.city = owner.city!!
//            }
//
//        }else{
//            println("Owner is null")
//        }
//
//    }

    companion object {
        const val USER_ADDRESS = "userAddress"
//        const val SENDING_NAME_ID = "mqttAndroidClientId"

        // Use this function to create instance of your fragment
        fun newInstance(userState: User): ExploreFragment {
            val args = Bundle()
            args.putString(USER_ADDRESS, userState.city)

//            args.putString(SENDING_NAME_ID , testClientId)
            val fragment = ExploreFragment()
            fragment.arguments = args
            return fragment
        }
    }



}