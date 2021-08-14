package com.example.bitcoin.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bitcoin.BR
import com.example.bitcoin.R
import com.example.bitcoin.databinding.ItemBitcoinBinding
import com.example.bitcoin.model.BitcoinModel
import java.util.*
import kotlin.collections.ArrayList

const val MAX_LIST_SIZE = 5

class BitcoinAdapter : RecyclerView.Adapter<BitcoinAdapter.BitcoinVH>() {

    var itemList : ArrayList<BitcoinModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BitcoinVH {
        val layout = DataBindingUtil.inflate<ItemBitcoinBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_bitcoin, parent, false
        )
        return BitcoinVH(layout)
    }

    override fun onBindViewHolder(holder: BitcoinVH, position: Int) {
        val item = itemList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun getData(bitcoinModel: BitcoinModel){
        itemList.add(bitcoinModel)
        notifyDataSetChanged()

        if (itemList.size> MAX_LIST_SIZE){
            itemList.removeAt(0)
            notifyDataSetChanged()
        }
    }


    inner class BitcoinVH(var itemBinding: ItemBitcoinBinding): RecyclerView.ViewHolder(itemBinding.root){

        fun bind(item: BitcoinModel){
            itemBinding.setVariable(BR.item, item)

            item.time?.let {
                val calendar = Calendar.getInstance(Locale.getDefault())
                calendar.timeInMillis = it * 1000L
                val date = DateFormat.format("dd-MM-yyyy HH:mm:ss",calendar).toString()
                itemBinding.tvTime.text = "Time : "+date
            }

        }
    }

}