package com.udacoding.ojolfirebasekotlin.utils

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.database.FirebaseDatabase
import com.udacoding.ojolfirebasekotlin.signup.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.udacoding.ojolfirebasekotlin.utama.home.model.Booking





object FirebaseHelper {


    private var auth: FirebaseAuth? = null


    fun insertRequest(
        tanggal: String, uid: String, lokasiAwal: String, latAwal: Double, lonAwal: Double, lokasiTujuan: String,
        latTujuan: Double, lonTujuan: Double, harga: String, jarak: String
    ): Boolean {


        val booking =Booking()
        booking.tanggal = tanggal
        booking.uid = uid
        booking.lokasiAwal = lokasiAwal
        booking.latAwal = latAwal
        booking.lonAwal = lonAwal
        booking.lokasiTujuan = lokasiTujuan
        booking.lonTujuan = lonTujuan
        booking.latTujuan = latTujuan
        booking.jarak = jarak
        booking.harga = harga
        booking.status = 1



        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference(Constan.tb_Booking)

        var key = database.reference.push().key





        myRef.child(key).setValue(booking)





        return true
    }


   fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)


        auth?.signInWithCredential(credential)?.addOnCompleteListener {
            task ->
            if(task.isSuccessful){

                val user = auth?.currentUser


                user?.displayName?.let { user.email?.let { it1 -> insertUser(it, it1,"08") } }
            }
            else{

            }
        }
    }

    fun insertUser(name: String, email: String, hp: String): Boolean {

        val uid = auth?.currentUser?.uid


        var user = Users()
        user.hp = hp
        user.email = email
        user.name = name
        user.uid = uid
        val database = FirebaseDatabase.getInstance()
        var key = database.reference.push().key
        val myRef = database.getReference(Constan.tb_Uaser)


        myRef.child(key).setValue(user)

        return true

    }



    fun autUserSignUp(email: String, pass: String): Boolean? {

        auth = FirebaseAuth.getInstance()
        var status: Boolean? = null

        auth?.createUserWithEmailAndPassword(email, pass)?.addOnCompleteListener { task ->

            if (task.isSuccessful) {

                status = true

            } else {

                status = false

            }


        }

        return status


    }

    fun authUserSignIn(email: String, pas: String): Boolean? {

        auth = FirebaseAuth.getInstance()
        var status: Boolean? = null

        auth?.signInWithEmailAndPassword(email, pas)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {

                status = true
            } else {

                status = false

            }
        }

        return true


    }

}