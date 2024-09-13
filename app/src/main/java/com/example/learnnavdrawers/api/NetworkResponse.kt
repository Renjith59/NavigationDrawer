package com.example.realweather.api

import androidx.core.app.NotificationCompat.MessagingStyle.Message

sealed class NetworkResponse<out T>{
    data class Success<out T>(val data : T) : NetworkResponse<T>()
    data class Error(val message: String) : NetworkResponse<Nothing>()
    object Loading : NetworkResponse<Nothing>()
}