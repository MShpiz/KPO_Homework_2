package com.example.features.decreaceMeal

import kotlinx.serialization.Serializable

@Serializable
data class DecreaseMealModerRequest(val token: ULong, val meal: String)
