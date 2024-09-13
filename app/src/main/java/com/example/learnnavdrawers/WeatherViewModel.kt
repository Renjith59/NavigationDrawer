package com.example.learnnavdrawers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realweather.api.Constant
import com.example.realweather.api.NetworkResponse
import com.example.realweather.api.RetrofitInstance
import com.example.realweather.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel(){

    private val weatherApi = RetrofitInstance.weatherApi
    private val weatherResults = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = weatherResults

    fun getData(city : String){
        weatherResults.value =  NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(Constant.apiKey,city)
                if (response.isSuccessful){
                    response.body()?.let {
                        weatherResults.value = NetworkResponse.Success(it)
                    }
                }else{
                    weatherResults.value = NetworkResponse.Error("Failed to load data")
                }
            }catch (e: Exception){
                weatherResults.value = NetworkResponse.Error("Failed to load data")
            }

        }


    }
}