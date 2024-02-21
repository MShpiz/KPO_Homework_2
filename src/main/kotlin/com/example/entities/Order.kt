package com.example.entities

class Order() {
    private val meals: MutableList<Meal> = mutableListOf()
    var state: OrderState = PreCookingState(this)
    fun getPrice(): UInt{
        var total: UInt = 0u
        for (meal in meals){
            total += meal.price
        }
        return total
    }
    fun addMeal(meal:Meal): Unit{
        state.addMeal(meal)
    }
    fun cook():Unit{
        state.cook()
    }

    fun cancel() {
        TODO("Not yet implemented")
    }

    fun removeMeal(meal: Meal) {

    }

}