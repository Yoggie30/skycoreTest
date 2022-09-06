package com.example.yogeshtestapplication.ui.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.yogeshtestapplication.R
import com.example.yogeshtestapplication.model.BusinessesItem
import com.example.yogeshtestapplication.ui.adapter.ItemListAdapter
import com.example.yogeshtestapplication.viewmodel.CommonViewModel
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var context: Context
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    var distance = 1500
    var latitude = 0.0
    var longitude = 0.0

    private var page = 0
    private var offset = 0
    private var itemsPg = 10
    private var totalList: ArrayList<BusinessesItem?>? = null

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this@MainActivity

        swipeLayout.setOnRefreshListener {
            page++
            Handler().postDelayed(Runnable {
                swipeLayout.isRefreshing = false
                offset = 0
                totalList!!.clear()
                recyclerView.visibility = View.GONE
                search(true)
            }, 2000)
        }

        seekBar?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                distance = seek.progress
                var distance1: String = seek.progress.toString()
                tvRadius.text = "$distance M    "
                if (distance >= 1000) {
                    tvRadius.text =
                        distance1.subSequence(0, 1).toString() + "." + distance1.subSequence(
                            1,
                            distance1.lastIndex
                        ) + " KM    "
                }
                Handler().postDelayed(Runnable {
                    offset = 0
                    search(false)
                }, 1500)
            }
        })
        seekBar.progress = 1500
        search(false)
        getLocation()

        idNestedSV.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                offset++
                search(true)
            }
        })

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { locationResult: Location? ->
                if (locationResult != null) {
                    currentLocation = locationResult
                    latitude = currentLocation!!.latitude
                    longitude = currentLocation!!.longitude
                    Log.e("location", currentLocation.toString())
                }
            }
        locationRequest = LocationRequest().apply {
            interval = TimeUnit.SECONDS.toMillis(60)
            fastestInterval = TimeUnit.SECONDS.toMillis(30)
            maxWaitTime = TimeUnit.MINUTES.toMillis(2)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult?.lastLocation?.let {
                    if (locationResult != null) {
                        currentLocation = locationResult.lastLocation
                        latitude = currentLocation!!.latitude
                        longitude = currentLocation!!.longitude
                        // use latitude and longitude as per your need
                        Log.e("location", currentLocation.toString())
                    }
                } ?: {
                    Log.e("location", "Location information isn't available.")
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )


    }


    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        var locationListener = android.location.LocationListener { location ->
            latitude = location!!.latitude
            longitude = location!!.longitude

            Log.i("location", "Latitute: $latitude ; Longitute: $longitude")
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode)
            return
        }
        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                getLocation()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun search(flag:Boolean) {
//        recyclerView.visibility = View.GONE
        val map: HashMap<String, Any> = HashMap<String, Any>()
        map["term"] = "restaurants"
        map["radius"] = distance
        map["sort_by"] = "distance"
        map["limit"] = itemsPg
        map["offset"] = offset * itemsPg

        if (!flag && latitude != null && latitude != 0.0 && longitude != null && longitude != 0.0) {
            map["latitude"] = latitude
            map["longitude"] = longitude
        } else {
            map["location"] = "nyc"
        }

        CommonViewModel().search(context, map)
            .observe(this) {
                it?.run {
                    it.let {
                        if (it.businesses != null && it.businesses.isNotEmpty()) {
                            for (item in it.businesses) {
                                if (totalList == null)
                                    totalList = ArrayList()
                                totalList!!.add(item)
                            }
                            showList(totalList)
                        } else {
                            alert()
                        }

                    }
                }
            }
    }

    private fun showList(data: List<BusinessesItem?>?) {
        if (data != null && data.isNotEmpty()) {
            var itemListAdapter =
                ItemListAdapter(this@MainActivity, data as ArrayList<BusinessesItem>)
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager =
                LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            recyclerView.adapter = itemListAdapter
            recyclerView.visibility = View.VISIBLE
//            recyclerView.scrollToPosition(offset)

//            Handler().postDelayed(Runnable {
//                call = true
//            }, 2000)

//            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
////                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
////                    super.onScrollStateChanged(recyclerView, newState)
////                    if (!recyclerView.canScrollVertically(1) && !isLoading) {
////                        Toast.makeText(context, "Last", Toast.LENGTH_LONG).show()
////                        offset++
////                        search(true)
////                        isLoading = true
////                    }
////                }
//
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    super.onScrolled(recyclerView, dx, dy)
//                    if (dy > 0) {
//                        Toast.makeText(context, "First", Toast.LENGTH_LONG).show()
//                    } else {
//                        Toast.makeText(context, "Last", Toast.LENGTH_LONG).show()
//                        offset++
//                        search(true)
////                        isScroll = true
//                    }
//                }
//            })

        }
    }

    fun alert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alert")
        builder.setMessage("There are no records for your location. Click Okay to continue with default location.")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            search(true)
        }
        builder.show()
    }

    private fun isLocationPermissionGranted(): Boolean {
        return if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                3333
            )
            false
        } else {
            true
        }
    }


}

