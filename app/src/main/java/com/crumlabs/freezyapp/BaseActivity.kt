package com.crumlabs.freezyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_base.*

class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        val exploreFragment=ExploreFragment()
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