package com.udacoding.ojolfirebasekotlin.utama.home


import android.annotation.SuppressLint
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory

import com.udacoding.ojolfirebasekotlin.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_home.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.health.TimerStat
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.nandohusni.baggit.network.NetworkModule
import com.udacoding.ojolfirebasekotlin.network.RequestNotificaton
import com.udacoding.ojolfirebasekotlin.utama.home.model.Booking
import com.udacoding.ojolfirebasekotlin.utama.home.model.ResultRoute
import com.udacoding.ojolfirebasekotlin.utama.home.model.RoutesItem
import com.udacoding.ojolfirebasekotlin.utils.ChangeFormat
import com.udacoding.ojolfirebasekotlin.utils.Constan
import com.udacoding.ojolfirebasekotlin.utils.DirectionMapsV2
import com.udacoding.ojolfirebasekotlin.utils.GPSTracker
import com.udacoding.ojolfirebasekotlin.waiting.WaitingDriverActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*

class HomeFragment : Fragment(), OnMapReadyCallback {

    var tanggal: String? = null
    var latAwal: Double? = null
    var lonAwal: Double? = null
    var latAkhir: Double? = null
    var lonAkhir: Double? = null
    var jarak: String? = null
    var dialog: Dialog? = null

    var keyy: String? = null
    private var auth: FirebaseAuth? = null
    var map: GoogleMap? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        auth = FirebaseAuth.getInstance()



        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)


        showPermission()

        visibleView(false)

        homeAwal.onClick {
            takeLocation(1)

        }
        homeTujuan.onClick {
            takeLocation(2)

        }


        homebuttonnext.onClick { view ->
            if (homeAwal.text.isNotEmpty() && homeTujuan.text.isNotEmpty()) {

                showInserServer()
            } else {

                view?.let { Snackbar.make(it, "Tidak boleh kosong", Toast.LENGTH_SHORT).show() }

            }

        }
        // mapView?.getMapAsync(this);
    }

    fun bookingHistoryUser(key: String) {


        toast("hheiheiei")

        showDialog(true)


        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference(Constan.tb_Booking)


        myRef.child(key).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(p0: DataSnapshot?) {


                val booking = p0?.getValue(Booking::class.java)


                toast(booking?.status.toString())
//
                if (booking?.status == 2) {
                    startActivity<WaitingDriverActivity>(Constan.Key to key)
                    showDialog(false)


                }


            }

        })


    }

    private fun showInserServer() {

        val currentTime = Calendar.getInstance().time
        tanggal = currentTime.toString()

        if (latAwal?.let {
                lonAwal?.let { it1 ->
                    latAkhir?.let { it2 ->
                        lonAkhir?.let { it3 ->
                            jarak?.let { it4 ->
                                insertRequest(
                                    currentTime.toString(), auth?.uid.toString(),

                                    homeAwal.text.toString(),
                                    it, it1, homeTujuan.text.toString(), it2, it3,

                                    homeprice.text.toString(), it4
                                )
                            }
                        }
                    }
                }
            }!!
        ) {


        }


    }


    fun insertRequest(
        tanggal: String, uid: String, lokasiAwal: String, latAwal: Double, lonAwal: Double, lokasiTujuan: String,
        latTujuan: Double, lonTujuan: Double, harga: String, jarak: String
    ): Boolean {


        val booking = Booking()
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
        booking.driver = ""


        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference(Constan.tb_Booking)

        keyy = database.reference.push().key


        val hei = keyy

        pushNotif(booking)
        hei?.let { bookingHistoryUser(it) }
        myRef.child(keyy).setValue(booking)







        return true
    }

    private fun pushNotif(booking: Booking) {


        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Driver")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(p0: DataSnapshot?) {

                for (issue in p0?.children!!) {

                    val token = issue.child("token").getValue(String::class.java)!!

                    val request = RequestNotificaton()
                    request.token = token
                    request.sendNotificationModel = booking

                    NetworkModule.getServiceFcm().sendChatNotification(request)
                        .enqueue(object : Callback<ResponseBody> {
                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                                Log.d("network failed : ", t.message)
                            }

                            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {

                                Log.d("response server:", response.message())
                            }
                        })
                }


            }
        })


    }


    private fun showDialog(status: Boolean) {
        dialog = Dialog(context)
        dialog?.setContentView(R.layout.dialogwaitingdriver)

        if (status) {


            dialog?.show()

        } else dialog?.dismiss()


    }

    fun showPermission() {

        showGps()
        if (activity?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED) {

            if (activity?.let {
                    ActivityCompat.shouldShowRequestPermissionRationale(
                        it,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    )
                }!!) {


                showGps()
            } else {

                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ), 1
                )

            }
        }

    }

    private fun showGps() {

        val gps = GPSTracker(context)
        if (gps.canGetLocation()) {
            latAwal = gps.latitude
            lonAwal = gps.longitude

            val name = showName(latAwal ?: 0.0, lonAwal ?: 0.0)

            homeAwal.text = name

        } else gps.showSettingGps()


    }

    fun visibleView(status: Boolean) {

        if (status) {
            homebottom.visibility = View.VISIBLE
            homebuttonnext.visibility = View.VISIBLE

        } else {

            homebottom.visibility = View.GONE
            homebuttonnext.visibility = View.GONE
        }
    }

    fun takeLocation(status: Int) {

        try {
            val intent = PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                .build(activity)
            startActivityForResult(intent, status)
        } catch (e: GooglePlayServicesRepairableException) {
            // TODO: Handle the error.
        } catch (e: GooglePlayServicesNotAvailableException) {
            // TODO: Handle the error.
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(activity, data)
                latAwal = place.latLng.latitude
                lonAwal = place.latLng.longitude

                homeAwal.text = place.address.toString()
                showMarker(latAwal ?: 0.0, lonAwal ?: 0.0, place.address.toString())
                Log.i("locations", "Place: " + place.name)
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(activity, data)
                // TODO: Handle the error.
                Log.i("locatios", status.statusMessage)

            } else if (resultCode == RESULT_CANCELED) {
            }
        } else {

            if (resultCode == RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(activity, data)

                latAkhir = place.latLng.latitude
                lonAkhir = place.latLng.longitude

                showMarker(latAkhir ?: 0.0, lonAkhir ?: 0.0, place.address.toString())

                homeTujuan.text = place.address.toString()

                route()
                Log.i("locations", "Place: " + place.name)
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                val status = PlaceAutocomplete.getStatus(activity, data)
                // TODO: Handle the error.
                Log.i("locatios", status.statusCode.toString())


            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }

        }
    }

    @SuppressLint("CheckResult")
    private fun route() {


        val origin = latAwal.toString() + "," + lonAwal.toString()
        val dest = latAkhir.toString() + "," + lonAkhir.toString()
        NetworkModule.getService()
            .actionRoute(origin, dest, getString(R.string.google_api_key))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ t: ResultRoute? ->
                showData(t?.routes)
            },
                {})

    }

    private fun showData(routes: List<RoutesItem?>?) {

        visibleView(true)

        if (routes != null) {

            val point = routes.get(0)?.overviewPolyline?.points

            jarak = routes.get(0)?.legs?.get(0)?.distance?.text
            val jarakValue = routes.get(0)?.legs?.get(0)?.distance?.value
            val waktu = routes.get(0)?.legs?.get(0)?.duration?.text

            homeWaktudistance.text = waktu + " ( " + jarak + " )"

            val pricex = jarakValue?.toDouble()?.let { Math.round(it) }


            val price = pricex?.div(1000.0)?.times(2000.0)

            val price2 = ChangeFormat.toRupiahFormat2(price.toString())

            homeprice.text = "Rp. " + price2

            map?.let { point?.let { it1 -> DirectionMapsV2.gambarRoute(it, it1) } }

        }


    }


    fun showName(lat: Double, lon: Double): String {

        var name = ""
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(lat, lon, 1);

            if (addresses.size > 0) {
                val fetchedAddress = addresses.get(0);
                val strAddress = StringBuilder()

                for (i in 0..fetchedAddress.maxAddressLineIndex) {
                    name = strAddress.append(fetchedAddress.getAddressLine(i)).append(" ").toString()


                    ;

                }

            }

        } catch (e: Exception) {

        }




        return name
    }


    fun showMarker(lat: Double, lon: Double, msg: String) {

        val coordinate = LatLng(lat, lon)

        map?.addMarker(MarkerOptions().position(coordinate).title(msg))
        map?.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 16f))
        map?.moveCamera(CameraUpdateFactory.newLatLng(coordinate))
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(p0: GoogleMap?) {
        map = p0;
        map?.getUiSettings()?.setMyLocationButtonEnabled(false)
        //  map?.setMyLocationEnabled(true)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-6.3088652, 106.682188), 12f))
    }

    //    @Override
// public void onResume() {
//  super.onResume();
// val autoUpdate = Timer()
    // updates each 40 secs
// }
//


    override fun onResume() {
        mapView?.onResume()
        super.onResume()
        val autoUpdate = Timer()

        autoUpdate.schedule(object : TimerTask() {
            override fun run() {
                activity?.runOnUiThread(Runnable {



                        keyy?.let { bookingHistoryUser(it) }

                })


            }

        }, 1000)
    }


    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1) {

            showGps()
        }
    }


}
