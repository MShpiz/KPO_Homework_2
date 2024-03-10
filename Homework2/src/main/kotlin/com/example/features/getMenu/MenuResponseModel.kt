package com.example.features.getMenu

import com.example.entities.Meal
import kotlinx.serialization.Serializable

@Serializable
data class MenuResponseModel(val menu: List<Meal>)