package com.example.climatic.map.view


import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.example.climatic.R
import com.example.climatic.favourites_screen.viewmodel.FavouriteViewModel
import com.example.climatic.favourites_screen.viewmodel.FavouriteViewModelFactory
import com.example.climatic.home_screen.viewmodel.HomeViewModelFactory
import com.example.climatic.model.database.LocalDataSourceImpl
import com.example.climatic.model.database.WeatherDB
import com.example.climatic.model.dtos.Favourites
import com.example.climatic.model.network.RemoteDataSourceImpl
import com.example.climatic.model.repository.Repository
import com.example.climatic.model.repository.RepositoryImpl
import com.example.climatic.model.responses.ForecastResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.osmdroid.api.IGeoPoint
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.library.BuildConfig
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import java.io.IOException
import java.util.Locale


class MapActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var editTextSearch: EditText
    private lateinit var btnAddLocation: Button
    private lateinit var favouriteViewModel: FavouriteViewModel
    lateinit var  favouriteViewModelFactory : FavouriteViewModelFactory
    private var lat : Double = 30.0;
    private var long: Double = 31.0;
    //private var lastSelectedGeoPoint: IGeoPoint? = null // To hold the last selected location
    private lateinit var marker: Marker // To hold the last added marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favouriteViewModelFactory = FavouriteViewModelFactory(
            RepositoryImpl.getInstance(
                RemoteDataSourceImpl.getInstance(),
                LocalDataSourceImpl.getInstance(WeatherDB.getInstance(this).dao())
            )
        )
        favouriteViewModel = ViewModelProvider(this, favouriteViewModelFactory).get(FavouriteViewModel::class.java)
       // Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        setContentView(R.layout.activity_map)
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
//editTextSearch = findViewById(R.id.edit_text_search)
        btnAddLocation = findViewById(R.id.btn_add_location)
        mapView = findViewById(R.id.map)
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(10.0)
        val defaultLocation = GeoPoint(30.0, 31.00)
        mapView.controller.setCenter(defaultLocation)
        marker = Marker(mapView)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        mapView.overlays.add(marker)
        mapView.overlays.add(marker)

        val mapEventsOverlay = MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                setMarker(p)
                lat = p.latitude
                long = p.longitude
                return true
            }
            override fun longPressHelper(p: GeoPoint): Boolean = false
        })
        mapView.overlays.add(mapEventsOverlay)
        btnAddLocation.setOnClickListener {
            favouriteViewModel.addFavorite(Favourites( lat = lat, lon = long))
        }
        //setupMap()
        //setupAddLocationButton()
    }

//    private fun setupMap() {
//        mapView.setTileSource(TileSourceFactory.MAPNIK)
//        mapView.setBuiltInZoomControls(true)
//        mapView.setMultiTouchControls(true)
//        //mapView.onCreate(null)
//
//        // Example: Adding a marker on map click
//        mapView.setOnClickListener { event ->
//            // Get clicked location and add a marker
//            val geoPoint = mapView.projection.fromPixels(event.x.toInt(), event.y.toInt())
//            addMarker(geoPoint)
//        }
//    }
//
//    private fun addMarker(geoPoint: IGeoPoint) {
//        lastSelectedGeoPoint = geoPoint
//        lastMarker?.let {
//            mapView.overlays.remove(it)
//        }
//        lastMarker = Marker(mapView).apply {
//            position = geoPoint as GeoPoint
//            title = "Selected Location"
//            setOnMarkerClickListener { marker, _ ->
//                Toast.makeText(this@MapActivity, marker.title, Toast.LENGTH_SHORT).show()
//                true
//            }
//        }
//        mapView.overlays.add(lastMarker!!)
//        mapView.invalidate()
//        btnAddLocation.visibility = View.VISIBLE
//    }



//    private fun setupAddLocationButton() {
//        btnAddLocation.setOnClickListener {
//            lastSelectedGeoPoint?.let { geoPoint ->
//                val newLocation = ForecastResponse(
//                    latitude = geoPoint.latitude,
//                    longitude = geoPoint.longitude,
//                    name = "Selected Location"
//                )
//                viewModel.addFavorite(newLocation)
//                Toast.makeText(this, "Location added to favorites", Toast.LENGTH_SHORT).show()
//                btnAddLocation.visibility = View.GONE // Hide button after adding
//            }
//        }
//    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDetach()
    }
    private fun setMarker(point: GeoPoint) {
        marker.position = point
        marker.title = "Location: ${point.latitude}, ${point.longitude}"
        marker.icon = ContextCompat.getDrawable(this, R.drawable.custom_marker)
        mapView.invalidate()
    }
    fun getAddress(lat: Double, lon: Double): String {
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val currentAddress = StringBuilder("")
                if (!address.subAdminArea.isNullOrBlank()) currentAddress.append(address.subAdminArea)
                return currentAddress.toString()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }
}

