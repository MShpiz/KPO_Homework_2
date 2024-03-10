package com.example.entities

import javax.naming.directory.InvalidAttributesException

class OrderBuilder {
    private var order: Order = Order()
    fun addMeal(mealId: Int){
        val meal: Meal
        try {
            meal = DBAdapter.getMeal(mealId)
        } catch (e: IndexOutOfBoundsException) {
            throw IllegalArgumentException("no such meal")
        } catch (e: Exception) {
            println(e.message)
            throw Exception("smth went wrong")
        }
        DBAdapter.decreaseMealAmount(meal.id)
        order.addMeal(meal)
    }

    fun removeMeal(mealId: Int) {

        try{
            order.removeMeal(DBAdapter.getMeal(mealId));
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