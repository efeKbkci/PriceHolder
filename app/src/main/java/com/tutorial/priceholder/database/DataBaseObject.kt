package com.tutorial.priceholder.database

import kotlinx.serialization.Serializable

@Serializable
data class DataBaseObject(
    val objectID:Long = 0,
    val forWho:String = "",
    val numberOfPortion:String = "",
    val priceForEachPortion:String = "",
    val taxiPrice:String = "",
    val mealOfDay:String = "",
    val noteForThatDay:String = "",
    val dateOfThatDay:String = ""
)