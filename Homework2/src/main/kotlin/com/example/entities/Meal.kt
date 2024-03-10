package com.example.entities

import kotlinx.serialization.Serializable

@Serializable
class Meal(val name: String, var cookingTime: UInt, var price: UInt, val id: Int) {
}