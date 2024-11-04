package com.example.weather.favourites_screen.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.climatic.map.view.MapActivity
import com.example.climatic.R
import com.example.climatic.favourites_screen.view.FavAdapter
import com.example.climatic.favourites_screen.viewmodel.FavouriteViewModel
import com.example.climatic.favourites_screen.viewmodel.FavouriteViewModelFactory
import com.example.climatic.home_screen.view.HomeFragment
import com.example.climatic.model.database.LocalDataSourceImpl
import com.example.climatic.model.database.WeatherDB
import com.example.climatic.model.dtos.Favourites
import com.example.climatic.model.network.RemoteDataSourceImpl
import com.example.climatic.model.repository.RepositoryImpl
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class FavouritesFragment : Fragment() {

    lateinit var favouriteViewModel: FavouriteViewModel
    lateinit var favouriteViewModelFactory: FavouriteViewModelFactory
    lateinit var favouritesAdapter: FavAdapter
    lateinit var rvFavourites: RecyclerView
    lateinit var fabAddLocation: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_favourites, container, false)

        favouriteViewModelFactory = FavouriteViewModelFactory(
            RepositoryImpl.getInstance(
                RemoteDataSourceImpl.getInstance(),
                LocalDataSourceImpl.getInstance(WeatherDB.getInstance(requireContext()).dao())
            )
        )
        favouriteViewModel = ViewModelProvider(this, favouriteViewModelFactory).get(FavouriteViewModel::class.java)

        fabAddLocation = root.findViewById(R.id.fab_add_location)
        rvFavourites = root.findViewById(R.id.rvFavourites)
        rvFavourites.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        favouritesAdapter = FavAdapter(myListener = { favourite ->
            val homeFragment = HomeFragment().apply {
                arguments = Bundle().apply {
                    putDouble("lat", favourite.lat)
                    putDouble("lon", favourite.lon)
                    putBoolean("isFavourite", true)
                }
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, homeFragment)
                .addToBackStack(null)
                .commit()
        },
            removeListener = { favourite ->
                favouriteViewModel.removeFavorite(Favourites(favourite.lat, favourite.lon))
            })
        rvFavourites.adapter = favouritesAdapter

        fabAddLocation.setOnClickListener {
            val intent = Intent(requireContext(), MapActivity::class.java)
            startActivity(intent)
        }

        // Observe weatherFavourites from ViewModel
        lifecycleScope.launch {
            favouriteViewModel.forecastState.collect { favourites ->
                (rvFavourites.adapter as FavAdapter).submitList(favourites)
            }
        }

        return root
    }
}
