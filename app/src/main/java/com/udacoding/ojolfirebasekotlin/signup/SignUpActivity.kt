package com.udacoding.ojolfirebasekotlin.signup

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.udacoding.ojolfirebasekotlin.login.LoginActivity
import com.udacoding.ojolfirebasekotlin.R
import com.udacoding.ojolfirebasekotlin.utils.Constan
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.startActivity

class SignUpActivity : AppCompatActivity() {

    private var auth: FirebaseAuth? = null
    var googleSignInClient : GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        auth = FirebaseAuth.getInstance()

        signUpbutton.onClick {

            if (signUpEmail.text.isNotEmpty() &&
                signUpHp.text.isNotEmpty() &&
                signUpName.text.isNotEmpty() &&
                signUpPassword.text.isNotEmpty() &&
                signUpPasswordConfirm.text.isNotEmpty()
            ) {


               autUserSignUp(signUpEmail.text.toString(), signUpPassword.text.toString())



                }


        }

    }

    fun autUserSignUp(email: String, pass: String): Boolean? {

        auth = FirebaseAuth.getInstance()
        var status: Boolean? = null

        auth?.createUserWithEmailAndPassword(email, pass)?.addOnCompleteListener { task ->

            if (task.isSuccessful) {

                if(insertUser(signUpName.text.toString(),
                        signUpEmail.text.toString(),
                        signUpHp.text.toString(),task.result.user)!!
                ){

                    startActivity<LoginActivity>()
                }



            } else {

                status = false

            }


        }

        return status


    }

    fun insertUser(
        name: String,
        email: String,
        hp: String,
        users: FirebaseUser
    ): Boolean {




        var user = Users()
        user.uid = users.uid
        user.name = name
        user.email = email
        user.hp = hp
        val database = FirebaseDatabase.getInstance()
        var key = database.reference.push().key
        val myRef = database.getReference(Constan.tb_Uaser)


        myRef.child(key).setValue(user)

        return true

    }





}
