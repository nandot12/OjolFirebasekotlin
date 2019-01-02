package com.udacoding.ojolfirebasekotlin.waiting

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.udacoding.ojolfirebasekotlin.R
import com.udacoding.ojolfirebasekotlin.utama.home.model.Booking
import com.udacoding.ojolfirebasekotlin.utils.Constan
import kotlinx.android.synthetic.main.activity_waiting_driver.*
import java.sql.Driver

class WaitingDriverActivity : AppCompatActivity(), OnMapReadyCallback {

    var key: String? = null
    var database :FirebaseDatabase? = null

    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting_driver)

        key = intent.getStringExtra(Constan.Key)







        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera


        database = FirebaseDatabase.getInstance()
        val myRef = database?.getReference(Constan.tb_Booking)

        myRef?.child(key)?.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot?) {

                for( issue in p0?.children!!){

                    val booking = issue.getValue(Booking::class.java)

                    showData(booking)
                }
            }

            override fun onCancelled(p0: DatabaseError?) {




            }
        })
    }

    private fun showData(booking: Booking?) {

        homeAwal.text = booking?.lokasiAwal
        homeTujuan.text = booking?.lokasiTujuan
        homeprice.text = booking?.harga + "( "+ booking?.jarak + ")"

       val myref = database?.getReference("Driver")

        val query = myref?.orderByChild("uid")?.equalTo(booking?.driver)
        query?.addListenerForSingleValueEvent(object  : ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(p0: DataSnapshot?) {
                for(issue in p0?.children!!){
                    var driver = com.udacoding.ojolfirebasekotlin.waiting.model.Driver()

                    val d = issue.getValue(com.udacoding.ojolfirebasekotlin.waiting.model.Driver::class.java)
                    driver.latitude = d?.latitude
                    driver.longitude = d?.longitude

                    showMarker(driver)




                }
            }
        })











    }

    private fun showMarker(driver: com.udacoding.ojolfirebasekotlin.waiting.model.Driver) {

        val sydney = driver.latitude?.toDouble()?.let { driver.longitude?.toDouble()?.let { it1 -> LatLng(it, it1) } }
        mMap.addMarker(sydney?.let { MarkerOptions().position(it).title(driver.name) })
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))


    }
}
