package com.example.bitcoin

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.bitcoin.databinding.ActivityMainBinding
import com.example.bitcoin.network.ApiConstants
import com.example.bitcoin.utils.ConnectivityReceiver
import com.example.bitcoin.viewmodel.BitcoinVM
import okhttp3.*


class MainActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel : BitcoinVM
    private lateinit var client : OkHttpClient
    private lateinit var listener : EchoWebSocketListener
    private lateinit var request: Request

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(BitcoinVM::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        client = OkHttpClient()
        registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        ConnectivityReceiver.connectivityReceiverListener = this
        start()
    }

    private fun start() {
        request = Request.Builder().url(ApiConstants.WEBSOCKET_URL).build()
        listener = EchoWebSocketListener()
        client.newWebSocket(request, listener)
    }

    inner class EchoWebSocketListener : WebSocketListener(){
        var webSocket: WebSocket? = null

        override fun onOpen(webSocket: WebSocket, response: Response) {
            viewModel.status.postValue("Connected..")
            this.webSocket = webSocket
            listener.webSocket?.send("{\n\"op\": \"unconfirmed_sub\"}")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("webSocket", text)
            runOnUiThread {
                viewModel.getData(text)
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            viewModel.status.postValue("Disconnected..")
            webSocket.close(1000, null)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            viewModel.status.postValue("Disconnected..")
            Log.e("Error","Error : $t.message")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ConnectivityReceiver.connectivityReceiverListener = null
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (!isConnected) {
            viewModel.status.postValue("Disconnected..")
            listener.webSocket?.close(1000, null)
        } else {
            client.newWebSocket(request, listener)
        }
    }

}