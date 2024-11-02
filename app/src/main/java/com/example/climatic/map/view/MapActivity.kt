package com.example.climatic.map.view

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.climatic.R
import com.example.climatic.favourites_screen.viewmodel.FavouriteViewModel
import com.example.climatic.favourites_screen.viewmodel.FavouriteViewModelFactory
import com.example.climatic.model.database.LocalDataSourceImpl
import com.example.climatic.model.database.WeatherDB
import com.example.climatic.model.network.NominatimApiService
import com.example.climatic.model.network.NominatimResult
import com.example.climatic.model.network.RemoteDataSourceImpl
import com.example.climatic.model.repository.RepositoryImpl
import com.example.climatic.map.viewmodel.MapViewModel
import com.example.climatic.map.viewmodel.MapViewModelFactory
import com.example.climatic.model.dtos.Favourites
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.library.BuildConfig
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.Locale

class MapActivity : AppCompatActivity() {
    private lateinit var mapView: MapView
    private lateinit var btnAddLocation: Button
    private lateinit var favouriteViewModel: FavouriteViewModel
    private lateinit var autocompleteCity: AutoCompleteTextView
    private lateinit var nominatimService: NominatimApiService
    private lateinit var autocompleteAdapter: ArrayAdapter<String>
    private val cityResults = mutableListOf<NominatimResult>()
    private var lat: Double = 30.0
    private var long: Double = 31.0
    private lateinit var marker: Marker
    private lateinit var mapViewModel: MapViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favouriteViewModel = FavouriteViewModelFactory(
            RepositoryImpl.getInstance(
                RemoteDataSourceImpl.getInstance(),
                LocalDataSourceImpl.getInstance(WeatherDB.getInstance(this).dao())
            )
        ).create(FavouriteViewModel::class.java)

        setContentView(R.layout.activity_map)
        autocompleteCity = findViewById(R.id.autocomplete_city)
        autocompleteAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line)
        autocompleteCity.setAdapter(autocompleteAdapter)

        setupRetrofit()  // Initialize Retrofit for Nominatim API
        val factory = MapViewModelFactory(nominatimService)
        mapViewModel = ViewModelProvider(this, factory).get(MapViewModel::class.java)

        setupAutocompleteSearch()
        observeCityResults() // Observe LiveData

        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID
        btnAddLocation = findViewById(R.id.btn_add_location)
        mapView = findViewById(R.id.map)
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(10.0)
        val defaultLocation = GeoPoint(30.0, 31.00)
        mapView.controller.setCenter(defaultLocation)
        marker = Marker(mapView)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
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
            Log.d("MapActivity", "Adding Favorite: lat = $lat, long = $long")
            favouriteViewModel.addFavorite(Favourites(lat = lat, lon = long))
        }
    }

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

    private fun setupRetrofit() {
        val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        val client = OkHttpClient.Builder().addInterceptor(logging).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        nominatimService = retrofit.create(NominatimApiService::class.java)
    }

    private fun setupAutocompleteSearch() {
        autocompleteCity.addTextChangedListener { editable ->
            val query = editable.toString()
            if (query.isNotBlank()) {
                mapViewModel.searchCity(query) // Call searchCity on ViewModel
            }
        }

        autocompleteCity.setOnItemClickListener { _, _, position, _ ->
            mapViewModel.cityResults.value?.let { results ->
                if (results.isNotEmpty()) {
                    val selectedResult = results[position]
                    val point = GeoPoint(selectedResult.lat.toDouble(), selectedResult.lon.toDouble())
                    setMarker(point)
                    mapView.controller.animateTo(point)

                    // Update lat and long with the selected city's coordinates
                    lat = selectedResult.lat.toDouble()
                    long = selectedResult.lon.toDouble()

                    Toast.makeText(this, "Selected: ${selectedResult.display_name}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun observeCityResults() {
        mapViewModel.cityResults.observe(this) { results ->
            val cityNames = results.map { it.display_name }
            autocompleteAdapter.clear()
            autocompleteAdapter.addAll(cityNames)
            autocompleteAdapter.notifyDataSetChanged()
            Log.d("MapActivity", "Number of results: ${results.size}")
        }
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
