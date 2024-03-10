package com.example.entities

import kotlinx.serialization.Serializable


@Serializable
data class UserActivity(val userId: Int, val action: Int, val time: String)