package com.example.dunipool.MarketAdapter

import android.annotation.SuppressLint
import android.support.annotation.ArrayRes
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dunipool.R

import com.example.dunipool.databinding.ItemRecyclerMarketBinding
import ir.dunijet.dunipool.apiManager.BASE_URL_IMAGE
import ir.dunijet.dunipool.apiManager.model.CoinData

class MarketAdapter(
    private val data: ArrayList<CoinData.Data>,
    private val recyclerCallBack: RecyclerCallBack

) : RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {
    lateinit var binding: ItemRecyclerMarketBinding

    inner class MarketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bindViews(dataCoin: CoinData.Data) {

            try {



                binding.txtCoinName.text = dataCoin.coinInfo.fullName
                binding.txtPrice.text = "$" + dataCoin.rAW.uSD.pRICE.toString()
                binding.txtMarketCap.text = dataCoin.rAW.uSD.mKTCAP.toString()





                val taghir = dataCoin.rAW.uSD.cHANGEPCT24HOUR
                if (taghir > 0) {
                    binding.txtTaghir.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.colorGain
                        )
                    )
                    binding.txtTaghir.text =
                        dataCoin.rAW.uSD.cHANGEPCT24HOUR.toString().substring(0, 4) + "%"
                } else if (taghir < 0) {
                    binding.txtTaghir.setTextColor(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.colorLoss
                        )
                    )
                    binding.txtTaghir.text =
                        dataCoin.rAW.uSD.cHANGEPCT24HOUR.toString().substring(0, 5) + "%"
                } else {
                    binding.txtTaghir.text = "0%"
                }








               // binding.txtTaghir.text = dataCoin.rAW.uSD.cHANGEPCT24HOUR.toString()
                Glide.with(itemView).load(BASE_URL_IMAGE + dataCoin.coinInfo.imageUrl)
                    .into(binding.imgItem)

                itemView.setOnClickListener {
                    recyclerCallBack.oncoinitemclicked(dataCoin)
                }
                val marketCap = dataCoin.rAW.uSD.mKTCAP / 1000000000
                val indexDot = marketCap.toString().indexOf('.')
                binding.txtMarketCap.text = "$" + marketCap.toString().substring(0 , indexDot + 3) + " B"

            } catch (ex: Exception) {
                Log.v("logProject", ex.message ?: "null error")
                Log.v("logProject", dataCoin.coinInfo.fullName)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ItemRecyclerMarketBinding.inflate(inflater, parent, false)
        return MarketViewHolder(binding.root)


    }


    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        holder.bindViews(data[position])

    }

    override fun getItemCount(): Int = data.size

    interface RecyclerCallBack {
        fun oncoinitemclicked(dataCoin: CoinData.Data)
        fun getaboutdatafromassets()
    }
}