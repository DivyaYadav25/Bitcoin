package com.example.bitcoin.model

import com.google.gson.annotations.SerializedName

data class UsdAmountResponse(

	@field:SerializedName("USD")
	val uSD: USD? = null
)

data class USD(

	@field:SerializedName("symbol")
	val symbol: String? = null,

	@field:SerializedName("last")
	val last: Double? = null,

	@field:SerializedName("buy")
	val buy: Double? = null,

	@field:SerializedName("sell")
	val sell: Double? = null,

	@field:SerializedName("15m")
	val jsonMember15m: Double? = null
)