package com.tutorial.demo.ui

import android.annotation.SuppressLint
import android.os.Bundle
import coil.load
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.tutorial.demo.BuildConfig
import com.tutorial.demo.R
import com.tutorial.demo.data.placesSearch.PlacesSearch
import com.tutorial.demo.databinding.ActivityPlacesBinding
import com.tutorial.demo.network.ApiClient
import com.tutorial.demo.utils.Method
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlacesActivity : BaseActivity() {
    companion object {
        private const val TAG = "PlacesActivity"
        private const val DEFAULT_ZOOM = 18F
        private const val DEFAULT_LATITUDE = 25.043871531367014
        private const val DEFAULT_LONGITUDE = 121.53453374432904
    }
    private val binding: ActivityPlacesBinding by lazy {
        ActivityPlacesBinding.inflate(layoutInflater)
    }

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        doInitialize()
        setActionBar()
        findNearSearch()
    }

    private fun doInitialize() {
        // 初始化地圖
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(mapCallback)
    }

    @SuppressLint("MissingPermission")
    private val mapCallback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        initGoogleMap()
        mMap.apply {
            uiSettings.setAllGesturesEnabled(true)
            isMyLocationEnabled = true
            uiSettings.isMyLocationButtonEnabled = true
            uiSettings.isMapToolbarEnabled = true
        }
    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun initGoogleMap() {
        if (!::mMap.isInitialized) return
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE), DEFAULT_ZOOM))
        mMap.setOnMarkerClickListener { marker ->
            val mask = marker.snippet.toString().split(",")
            val placeId = mask[0]
            val name = mask[1]
            val photoReference = mask[2]
            binding.apply {
                tvPreview.text = name
                // 讀取Google Place圖片方法
                if (photoReference.isNotEmpty())
                    imgPreview.load(
                        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photo_reference=" +
                                "$photoReference&key=${BuildConfig.GOOGLE_API_KEY}"
                    )
            }
            true
        }
    }

    private fun setActionBar() {
        binding.actionbar.apply {
            tvTitle.text = getString(R.string.actionbar_places)
            imgBack.setOnClickListener { finish() }
        }
    }

    private fun findNearSearch() {
        ApiClient.googlePlaces.getPlaceSearch(
            location = "$DEFAULT_LATITUDE,$DEFAULT_LONGITUDE",
            radius = "1000",
            type = "restaurant",
            key = BuildConfig.GOOGLE_API_KEY
        ).enqueue(object : Callback<com.tutorial.demo.data.placesSearch.PlacesSearch> {
            override fun onResponse(call: Call<com.tutorial.demo.data.placesSearch.PlacesSearch>, response: Response<com.tutorial.demo.data.placesSearch.PlacesSearch>) {
                response.body()?.let { res ->
                    res.results.forEach { result ->
                        CoroutineScope(Dispatchers.Main).launch {
                            val markerOption = MarkerOptions().apply {
                                position(LatLng(result.geometry.location.lat, result.geometry.location.lng))
                                title(result.name)
                                snippet("${result.place_id},${result.name},${result.photos[0].photo_reference}")
                            }
                            mMap.addMarker(markerOption)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<com.tutorial.demo.data.placesSearch.PlacesSearch>, t: Throwable) {
                t.printStackTrace()
                Method.logE(TAG, "onFailure: ${t.message}")
            }
        })
    }
}