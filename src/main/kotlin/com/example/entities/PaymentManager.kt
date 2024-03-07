package com.example.entities

class PaymentManager() {
    private fun validate(cardNumber: UInt): Boolean {
        return cardNumber in 10000000u..100000000u
    }
    fun checkOut(cardNumber: UInt, order: Order): Boolean {
        if (!validate(cardNumber)) return false
        try {
            DBAdapter.addPayment(order.getPrice())
        } catch (e: NullPointerException) {
            throw NullPointerException("Smth wrong withdatabase")
        } catch (e: Exception) {
            throw Exception("Smth went wrong")
        }
        return true
    }

}