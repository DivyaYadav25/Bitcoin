package com.example.bitcoin.network

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(ApiConstants.BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getApis():ApiList{
        return retrofit.create(ApiList::class.java)
    }

}