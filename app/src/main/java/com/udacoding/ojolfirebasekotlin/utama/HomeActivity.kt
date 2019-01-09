package com.udacoding.ojolfirebasekotlin.utama

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.udacoding.ojolfirebasekotlin.R
import com.udacoding.ojolfirebasekotlin.utama.profile.ProfileFragment
import com.udacoding.ojolfirebasekotlin.utama.history.HistoryFragment
import com.udacoding.ojolfirebasekotlin.utama.home.HomeFragment
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                setFragment(HomeFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {

                setFragment(HistoryFragment())
                return@OnNavigationItemSelectedListener true
            } R.id.navigation_profile -> {

                setFragment(ProfileFragment())
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setFragment(HomeFragment())

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    fun setFragment(fragment : Fragment){

        supportFragmentManager.beginTransaction().replace(R.id.container,fragment).commit()
    }
}
