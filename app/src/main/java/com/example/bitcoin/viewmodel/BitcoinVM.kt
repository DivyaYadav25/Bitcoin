package com.example.bitcoin.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bitcoin.adapter.BitcoinAdapter
import com.example.bitcoin.model.BitcoinModel
import com.example.bitcoin.network.ApiClient
import com.example.bitcoin.network.ApiList
import com.example.bitcoin.utils.Util
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import org.json.JSONTokener

const val MIN_DOLLAR_VALUE = 100

class BitcoinVM : ViewModel() {

    var adapter = BitcoinAdapter()
    var status = MutableLiveData("Connecting..")
    private var postApi : ApiList = ApiClient.getApis()
    private var usdAmount = 0.0

    init {
        callUsdConversionApi()
    }

    fun clearClicked(){
        adapter.itemList.clear()
        adapter.notifyDataSetChanged()
    }

    fun getData(text: String) {
        val bitcoinModel = BitcoinModel()
        var finalAmt = 0.0

        val jsonObject = JSONTokener(text).nextValue() as JSONObject
        val jsonObject2 = jsonObject.getJSONObject("x")

        val hash = jsonObject2.getString("hash")
        val time = jsonObject2.getString("time").toLongOrNull()

        val jsonArray = jsonObject2.getJSONArray("out")
        for (i in 0 until jsonArray.length()) {
            val satoshiAmount = jsonArray.getJSONObject(i).getString("value")
            val bitcoinAmount = Util.convertToBitcoin(satoshiAmount)

            val convertedUsdAmt = Util.convertToUsd(usdAmount,bitcoinAmount)
            if (convertedUsdAmt >MIN_DOLLAR_VALUE){
                finalAmt += convertedUsdAmt
            }
        }

        bitcoinModel.amount = finalAmt
        bitcoinModel.hash = hash
        bitcoinModel.time = time
        adapter.getData(bitcoinModel)
    }

    private fun callUsdConversionApi() {
        postApi.convertToUsd()
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { Log.d("ApicallStart","ApicallStart") }
            .doOnTerminate{Log.d("ApicallEnd","ApicallEnd")}
            .subscribe({
                if (it.uSD!=null){
                    usdAmount = it.uSD.last?:0.0
                }

            },{
                Log.e("Error","${it.message}")
            })
    }
}