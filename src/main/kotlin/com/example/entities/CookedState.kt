package com.example.entities

class CookedState(private val order: Order):OrderState {
    override fun addMeal(meal: Meal) {
        throw NoSuchMethodException("can't add meal to a finnished order")
    }

    override fun cook() {
        throw NoSuchMethodException("can't cook a finnished order")
    }
}