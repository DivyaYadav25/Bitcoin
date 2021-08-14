package com.example.bitcoin.utils

import android.util.Log
import java.lang.NumberFormatException
import java.math.BigDecimal

const val SATOSHI_CONVERTER = 1000000000

object Util {
    fun convertToBitcoin(satoshiAmount: String?) : Double{
        var bitcoinAmount = 0.0
        if (!satoshiAmount.isNullOrBlank() && !satoshiAmount.isNullOrEmpty()) {
            try {
                val amt = BigDecimal(satoshiAmount).divide(BigDecimal(SATOSHI_CONVERTER))
                bitcoinAmount = amt.toDouble()
            } catch (e: NumberFormatException) {
                Log.e("NumberFormatException", "NumberFormatException ${e.message}")
            }
        }

        return bitcoinAmount
    }

    fun convertToUsd(dollarValue:Double,bitcoinAmount:Double):Double{
        var usd = 0.0
        try {
            val amt = BigDecimal(dollarValue).times(BigDecimal(bitcoinAmount))
            usd = amt.toDouble()
        } catch (e: NumberFormatException) {
            Log.e("NumberFormatException", "NumberFormatException ${e.message}")
        }
        return usd
    }
}