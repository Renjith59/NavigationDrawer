package com.example.learnnavdrawers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.provider.FontsContractCompat.Columns
import coil.compose.AsyncImage
import com.example.realweather.api.NetworkResponse
import com.example.realweather.api.WeatherModel

@Composable
fun WeatherPage(viewModel: WeatherViewModel){

    var city by remember {
        mutableStateOf("")
    }
    val weatherResult = viewModel.weatherResult.observeAsState()
    val keyBoardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier.fillMaxWidth().padding(8.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = city,
                onValueChange = {
                    city = it
                },
                label = {
                    Text(text = "Search for any location")
                }
            )
            IconButton(onClick = {
                viewModel.getData(city)
                keyBoardController?.hide()
            }) {
                Icon(imageVector = Icons.Default.Search ,
                    contentDescription = "Search for any location"
                )
            }
        }

        when(val result=weatherResult.value){
            is NetworkResponse.Error -> {
               Text(text = result.message)
            }
            NetworkResponse.Loading -> {
                CircularProgressIndicator()
            }
            is NetworkResponse.Success -> {
//                Text(text = result.data.toString())
                WeatherDetails(data = result.data)
            }
            null -> {}
        }
    }

}

@Composable
fun WeatherDetails(data: WeatherModel){
Column (
    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
){
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ){
        Icon(
            imageVector = Icons.Default.LocationOn  ,
            contentDescription = "Location Icon",
            modifier = Modifier.size(40.dp)
        )
        Text(text = data.location.name, fontSize = 30.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = data.location.country, fontSize = 18.sp, color = Color.Gray)
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "${data.current.temp_c} ° c",
        fontSize = 56.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )

    AsyncImage(
        modifier = Modifier.size(160.dp),
        model = "https:${data.current.condition.icon}".replace("64x64","128x128"),
        contentDescription = "Condition Icon" )

    Text(
        text = data.current.condition.text,
        fontSize = 20.sp,
        textAlign = TextAlign.Center,
        color = Color.Gray
    )

    Spacer(modifier = Modifier.height(16.dp))
    Card{
        Column(
            modifier = Modifier.fillMaxWidth()){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeatherKeyValue("Humidity",data.current.humidity)
                WeatherKeyValue("Wind Speed",data.current.wind_kph+" km/h")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeatherKeyValue("UV",data.current.uv)
                WeatherKeyValue("Percipation",data.current.precip_mm+" mm")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeatherKeyValue("Local Time",data.location.localtime.split(" ")[1])
                WeatherKeyValue("Local Date",data.location.localtime.split(" ")[0])
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                WeatherKeyValue("Wind Gust",data.current.gust_kph+" km/hr")
                WeatherKeyValue("Visibility",data.current.vis_km+" km")
            }

        }
}

}
}

@Composable
fun WeatherKeyValue(key:String,value: String){

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = key, fontWeight = FontWeight.SemiBold,color=Color.Gray)
    }

}

