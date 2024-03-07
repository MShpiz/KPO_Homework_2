package com.example.entities

import javax.naming.directory.InvalidAttributesException

class OrderBuilder {
    private var order: Order = Order()
    private val dbAdapter: DBAdapter = DBAdapter()
    fun addMeal(mealId: Int){
        val meal: Meal
        try {
            meal = dbAdapter.getMeal(mealId)
        } catch (e: IndexOutOfBoundsException) {
            throw IllegalArgumentException("no such meal")
        }
        dbAdapter.decreaseMealAmount(meal.id)
        order.addMeal(meal)
    }

    fun removeMeal(mealName: String) {

        try{
            order.removeMeal(dbAdapter.getMeal(mealName));
        } catch (e: InvalidAttributesException) {
            throw NoSuchMethodException("can't remove meal")
        } catch (e: IndexOutOfBoundsException) {
            throw e
        }

    }

    fun cookOrder(): Unit { //
        order.cook()
    }

    fun cancelOrder() {
        order.cancel()
        order = Order()
    }

    fun getOrder(): Order? {
        if (order.state is CookedState) return order
        return null
    }


}