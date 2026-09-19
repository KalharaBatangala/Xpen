package com.example.xpen.util

object CurrencyUtils {
    fun format(amount: Double): String = "Rs: %.2f".format(amount)
}