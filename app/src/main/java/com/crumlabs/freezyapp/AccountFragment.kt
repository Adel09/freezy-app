package com.crumlabs.freezyapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_account.*


class AccountFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private lateinit var auth: FirebaseAuth
    lateinit var user: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth = Firebase.auth
        user = auth.currentUser!!

        val view = inflater.inflate(R.layout.fragment_account, container, false)
        val signOutBtn = view.findViewById<Button>(R.id.signOutBtn)
        val myName = view.findViewById<TextView>(R.id.owner_name_account)
        myName.text = user.displayName


        signOutBtn.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
        }

        return view

    }



}