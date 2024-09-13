package com.example.learnnavdrawers



sealed class Screens (val screen: String){
    data object Home: Screens("home")
    data object Profile: Screens("profile")
    data object Settings: Screens("settings")
    data object Weather : Screens("weather")
}
