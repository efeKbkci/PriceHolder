package com.tutorial.priceholder.navigator

import androidx.navigation.NavHostController
import com.tutorial.priceholder.navigator.AllDestinations.CALCULATING
import com.tutorial.priceholder.navigator.AllDestinations.HOME
import com.tutorial.priceholder.navigator.AllDestinations.SETTINGS
import com.tutorial.priceholder.navigator.AllDestinations.STORAGE

object AllDestinations{
    const val HOME:String = "Home"
    const val STORAGE:String = "Storage"
    const val SETTINGS:String = "Settings"
    const val CALCULATING:String = "Calculating"
}

class NavigateInMyApp (private val navController: NavHostController){

    fun navigateToHome(){
        navController.navigate(HOME)
    }
    fun navigateToStorage(){
        navController.navigate(STORAGE)
    }
    fun navigateToSettings(){
        navController.navigate(SETTINGS)
    }
    fun navigateToCalculating(){
        navController.navigate(CALCULATING){
            restoreState = true
            launchSingleTop = true
        }
    }
}