package com.example.entities

import javax.naming.directory.InvalidAttributesException

class OrderBuilder {
    private var order: Order = Order()
    private val dbAdapter: DBAdapter = DBAdapter()
    fun addMeal(mealName: String){
        val meal: Meal
        try {
            meal = dbAdapter.getMeal(mealName)
        } catch (e: IndexOutOfBoundsException) {
            throw IllegalArgumentException("no such meal")
        }
        dbAdapter.decrementMeal(meal.id)
        order.addMeal(meal)
    }

    fun removeMeal(mealName: String) {

        try{
            order.removeMeal(dbAdapter.getMeal(mealName));
        } catch (e: ) {
            throw NoSuchMethodException("can't remove meal")
        } catch (e: IndexOutOfBoundsException) {
            throw e
        }

    }

    fun cookOrder(): Order { //
        order.cook()
        return order
    }

    fun cancelOrder() {
        order.cancel()
        order = Order()
    }


}