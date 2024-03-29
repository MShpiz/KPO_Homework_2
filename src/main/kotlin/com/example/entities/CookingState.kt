package com.example.entities

import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class CookingState(private val order: Order): OrderState {
    private var needsCooking: UInt = 0u
    private var isCooking = false
    private var isDone = false
    private val lock = ReentrantLock()
    init {
        for (meal in order.meals) {
            needsCooking += meal.cookingTime
        }
    }

    private fun cookThread() {
        isCooking = true
        var time: Long
        do {
            lock.withLock {
                time = needsCooking.toLong()
                needsCooking = 0u
            }


            Thread.sleep(time)
        } while (time > 0)
        lock.withLock {
            isDone = true
            order.state = CookedState(order)
        }
    }

    override fun addMeal(meal: Meal) {
        lock.withLock {
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