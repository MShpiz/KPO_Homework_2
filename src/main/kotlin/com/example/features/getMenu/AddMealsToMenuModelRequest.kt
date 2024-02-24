package com.example.features.getMenu

import kotlinx.serialization.Serializable

@Serializable
class AddMealsToMenuModelRequest(val token: ULong, val itemName: String, val itemPrice: UInt, val itemAmount: UInt = 0u)