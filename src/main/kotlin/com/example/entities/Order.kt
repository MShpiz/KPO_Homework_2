package com.example.entities

class Order() {
    val meals: MutableList<Meal> = mutableListOf()
    fun getPrice(): UInt{
        var total: UInt = 0u
        for (meal in meals){
            total += meal.price
        }
        return total
    }

}