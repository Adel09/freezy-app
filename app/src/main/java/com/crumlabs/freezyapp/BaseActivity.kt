package com.crumlabs.freezyapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.crumlabs.freezyapp.models.User
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_base.*

class BaseActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        auth = Firebase.auth
        user = auth.currentUser!!

        database = Firebase.database.reference
        val ownerQuery: DatabaseReference? = database.child("users").child(user.uid)
        lateinit var owner: User
        if (ownerQuery != null) {
            println("Owner query not null")
            ownerQuery.get().addOnSuccessListener {
                println("reached onsuccesslistener")
                owner = it.getValue<User>()!!
                print(owner?.name)
                progressBarBase.visibility = View.GONE
                val exploreFragment=ExploreFragment.newInstance(owner)
                val favoriteFragment=FavoriteFragment()
                val uploadFragment = UploadFragment()
                val chatFragment=ChatsFragment()
                val accountFragment=AccountFragment()

                setCurrentFragment(exploreFragment)

                bottomnavigation.setOnNavigationItemSelectedListener {
                    when(it.itemId){
                        R.id.explore->setCurrentFragment(exploreFragment)
                        R.id.faves->setCurrentFragment(favoriteFragment)
                        R.id.newp -> setCurrentFragment(uploadFragment)
                        R.id.messages->setCurrentFragment(chatFragment)
                        R.id.account->setCurrentFragment(accountFragment)

                    }
                    true
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
                val intent = Intent(this, NoConnectionActivity::class.java)
                startActivity(intent)
                println(it.message)
            }

        }else{
            Toast.makeText(this, "Please complete registration", Toast.LENGTH_LONG).show()
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            println("Owner is null")
        }




    }

    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}