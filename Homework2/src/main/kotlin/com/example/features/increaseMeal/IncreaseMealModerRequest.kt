package com.example.features.increaseMeal

import kotlinx.serialization.Serializable

@Serializable
data class IncreaseMealModerRequest(val token: ULong,
                                    val mealId: Int)
