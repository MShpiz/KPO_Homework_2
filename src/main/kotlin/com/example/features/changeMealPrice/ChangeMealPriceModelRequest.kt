package com.example.features.changeMealPrice

import kotlinx.serialization.Serializable

@Serializable
data class ChangeMealPriceModelRequest(val token: ULong, val mealId: Int, val price: UInt)
