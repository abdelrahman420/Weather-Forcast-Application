package com.example.climatic.main_screen.view

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.climatic.R
import com.example.climatic.alerts_screen.view.AlarmFragment
import com.example.climatic.home_screen.view.HomeFragment
import com.example.climatic.main_screen.viewmodel.MainViewModel
import com.example.climatic.settings_screen.view.SettingsFragment
import com.example.climatic.utils.LanguageManager
import com.example.weather.favourites_screen.view.FavouritesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.navView)

        mainViewModel.currentFragment.observe(this, Observer { fragment ->
            fragment?.let { replaceFragment(it) }
        })

        if (savedInstanceState == null) {
            mainViewModel.selectFragment(HomeFragment())
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> mainViewModel.selectFragment(HomeFragment())
                R.id.favourites -> mainViewModel.selectFragment(FavouritesFragment())
                R.id.alerts -> mainViewModel.selectFragment(AlarmFragment())
                R.id.settings -> mainViewModel.selectFragment(SettingsFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
    }
    override fun attachBaseContext(newBase: Context?) {
        val sharedPreferences = newBase?.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val selectedLanguage = sharedPreferences?.getString("language", "en") ?: "en"
        val context = newBase?.let { LanguageManager.updateLanguage(it, selectedLanguage) }
        super.attachBaseContext(context)
    }
}
