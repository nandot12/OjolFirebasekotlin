package com.udacoding.ojolfirebasekotlin.utama.history


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.udacoding.ojolfirebasekotlin.R
import com.udacoding.ojolfirebasekotlin.utama.history.adapter.Historydapter
import com.udacoding.ojolfirebasekotlin.utama.home.model.Booking
import com.udacoding.ojolfirebasekotlin.utils.Constan
import kotlinx.android.synthetic.main.fragment_history.*
import java.lang.IllegalStateException


class HistoryFragment : Fragment() {
    private var auth: FirebaseAuth? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()


        auth?.uid?.let { bookingHistoryUser(it) }
    }

    fun bookingHistoryUser(uid: String) {

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference(Constan.tb_Booking)

        val data = ArrayList<Booking>()
        val query = myRef.orderByChild("uid").equalTo(uid)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (issue in dataSnapshot.children) {

                    val dataFirebase = issue.getValue(Booking::class.java)


                    val booking = Booking()
                    booking.tanggal = dataFirebase?.tanggal
                    booking.uid = dataFirebase?.uid
                    booking.lokasiAwal = dataFirebase?.lokasiAwal
                    booking.latAwal = dataFirebase?.latAwal
                    booking.lonAwal = dataFirebase?.lonAwal
                    booking.latTujuan = dataFirebase?.latAwal
                    booking.lonTujuan = dataFirebase?.lonTujuan
                    booking.lokasiTujuan = dataFirebase?.lokasiTujuan
                    booking.jarak = dataFirebase?.jarak
                    booking.harga = dataFirebase?.harga
                    booking.status = dataFirebase?.status
                    data.add(booking)



                    showData(data)
                }


            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value

            }
        })

    }

    private fun showData(data: ArrayList<Booking>) {
        if (data != null) {

            try {

                recyclerview.adapter = Historydapter(data)
                recyclerview.layoutManager = LinearLayoutManager(context) as RecyclerView.LayoutManager?

            }catch (e : IllegalStateException){

            }

        }


    }


}
