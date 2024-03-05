package com.example.features.getUserActivity

import kotlinx.serialization.Serializable

@Serializable
data class ActivityModelResponse(val activity: List<UserActivity>)