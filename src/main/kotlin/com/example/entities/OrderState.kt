package com.example.entities

interface OrderState {
    fun addMeal(meal:Meal): Unit
    fun cook():Unit
}