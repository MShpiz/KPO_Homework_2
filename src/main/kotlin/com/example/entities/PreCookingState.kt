package com.example.entities

class PreCookingState(val order: Order): OrderState {

    override fun addMeal(meal: Meal) {
        TODO("Not yet implemented")
    }

    override fun cook() {
        order.state = CookingState(order)
        
    }
}