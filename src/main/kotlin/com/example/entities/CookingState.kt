package com.example.entities

class CookingState(val order: Order): OrderState {
    private var needsCooking: Any = 0u
    private var isCooking = false
    private var isDone = false
    init {
        for (meal in order.meals) {
            if (needsCooking is UInt) {
                (needsCooking as UInt) += meal.cookingTime
            }
        }
    }

    private fun cookThread() {
        isCooking = true
        var time: Long
        do {
            synchronized(needsCooking){
                time = needsCooking as Long
                (needsCooking as UInt) = 0u
            }
            Thread.sleep(time)
        } while (time > 0)
        synchronized(isDone) {
            isDone = true
            order.state = CookedState(order)
        }
    }

    override fun addMeal(meal: Meal) {
        synchronized(needsCooking) {
            if (isDone) throw Exception("Order just finnished cooking")
            order.meals.add(meal)
            needsCooking += meal.cookingTime
        }
    }

    override fun cook() {
        if (isCooking) return
        val thread = Thread {
            cookThread()
        }
        thread.isDaemon = true
        thread.start()
    }
}