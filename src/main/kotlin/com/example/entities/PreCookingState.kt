package com.example.entities

class PreCookingState(private val order: Order): OrderState {

    override fun addMeal(meal: Meal) {
        order.meals.add(meal)
    }

    override fun cook() {
        order.state = CookingState(order)
        order.state.cook()
    }
}