package com.crumlabs.freezyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.crumlabs.freezyapp.models.Chat
import com.crumlabs.freezyapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_contact_owner.*
import java.lang.Exception
import java.util.*

class ContactOwnerActivity : AppCompatActivity() {

    lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    lateinit var user: FirebaseUser
    lateinit var ownerID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_owner)

        database = Firebase.database.reference
        auth = Firebase.auth
        user = auth.currentUser!!
        chatToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val intent = intent
        ownerID = intent.getStringExtra("ownerId")!!
        val ownerQuery: DatabaseReference? = database.child("users").child(ownerID)
        lateinit var owner: User
        if (ownerQuery != null) {
            try {
                ownerQuery.get().addOnSuccessListener {
                    owner = it.getValue<User>()!!
                    print(owner?.name)
                    contactToolbar.title = owner.name
                    contactToolbar.subtitle = "Lives in ${owner.city}"
                }
            }catch (e: Exception){
                println(e.message)
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            }

        }else{
            println("Owner is null")
        }

    }

    fun sendMessage(view: View){
        val messageUUID = UUID.randomUUID().toString()
        val message = contactMessageBox.text
        val chatUID = "${user.uid}__${ownerID}"

        //This is for /userchats/uid/chatuid
        database.child("userChats").child(user.uid).child(chatUID)

        //Create /chatmessages/chatid/messageid/value
        database.child("chatmessages").child(chatUID)



        var members = arrayListOf<String>()
        members.add(user.uid)
        members.add(ownerID)
        val chat = Chat(id = chatUID, members = members)
        database.child("chats").child(chatUID).setValue(chat)


    }

}