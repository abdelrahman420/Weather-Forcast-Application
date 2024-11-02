package com.example.climatic.map.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.climatic.model.network.NominatimApiService
import com.example.climatic.model.network.NominatimResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapViewModel(private val nominatimService: NominatimApiService) : ViewModel() {

    private val _cityResults = MutableLiveData<List<NominatimResult>>()
    val cityResults: LiveData<List<NominatimResult>> get() = _cityResults

    fun searchCity(query: String) {
        nominatimService.searchCity(query = query).enqueue(object : Callback<List<NominatimResult>> {
            override fun onResponse(call: Call<List<NominatimResult>>, response: Response<List<NominatimResult>>) {
                if (response.isSuccessful) {
                    _cityResults.value = response.body() ?: emptyList()
                }
            }

            override fun onFailure(call: Call<List<NominatimResult>>, t: Throwable) {
                _cityResults.value = emptyList() // Handle failure case
            }
        })
    }
}
