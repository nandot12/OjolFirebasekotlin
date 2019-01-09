package com.udacoding.ojolfirebasekotlin.waiting

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.udacoding.ojolfirebasekotlin.R
import com.udacoding.ojolfirebasekotlin.utama.HomeActivity
import com.udacoding.ojolfirebasekotlin.utama.home.model.Booking
import com.udacoding.ojolfirebasekotlin.utils.Constan
import kotlinx.android.synthetic.main.activity_waiting_driver.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import java.sql.Driver

class WaitingDriverActivity : AppCompatActivity() {

    var key: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_driver)

    }


}
