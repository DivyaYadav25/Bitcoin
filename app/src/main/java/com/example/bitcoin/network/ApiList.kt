package com.example.bitcoin.network

import com.example.bitcoin.model.UsdAmountResponse
import io.reactivex.Observable
import retrofit2.http.GET

interface ApiList {
    @GET(ApiConstants.CONVERT_AMOUNT)
    fun convertToUsd() : Observable<UsdAmountResponse>
}