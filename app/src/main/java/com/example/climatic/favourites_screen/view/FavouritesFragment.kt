package com.example.weather.favourites_screen.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.climatic.map.view.MapActivity
import com.example.climatic.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FavouritesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_favourites, container, false)

        val fabAddLocation: FloatingActionButton = root.findViewById(R.id.fab_add_location)
        fabAddLocation.setOnClickListener {
            val intent = Intent(requireContext(), MapActivity::class.java)
            startActivity(intent)
        }

        return root
    }
}
