package com.udacoding.ojolfirebasekotlin.auth

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.FirebaseDatabase
import com.udacoding.ojolfirebasekotlin.R
import com.udacoding.ojolfirebasekotlin.utama.HomeActivity
import com.udacoding.ojolfirebasekotlin.utils.Constan
import kotlinx.android.synthetic.main.activity_autentikasi_hp.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class AutentikasiHpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autentikasi_hp)


        val key = intent.getStringExtra(Constan.Key)
        val database = FirebaseDatabase.getInstance()

        val myRef = database.getReference(Constan.tb_Uaser)

        authentikasisubmit.onClick {

            if(authentikasinomorhp.text.toString().isNotEmpty()){

                myRef.child(key).child("hp").setValue(authentikasinomorhp.text.toString())
                startActivity<HomeActivity>()

            }
            else toast("tidak boleh kosong")
        }






    }
}
