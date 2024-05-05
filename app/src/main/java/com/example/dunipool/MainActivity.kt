package com.example.dunipool

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dunipool.MarketAdapter.MarketAdapter
import com.example.dunipool.apiManager.model.CoinAboutItem
//import com.example.dunipool.apiManager.model.coinaboutdata

import com.example.dunipool.databinding.ActivityMainBinding
import com.google.gson.Gson
import ir.dunijet.dunipool.apiManager.ApiManager
//import ir.dunijet.dunipool.apiManager.model.CoinAboutData
import ir.dunijet.dunipool.apiManager.model.CoinData
import ir.dunijet.dunipool.apiManager.model.coinaboutdata

class MainActivity : AppCompatActivity(),MarketAdapter.RecyclerCallBack {
    lateinit var binding: ActivityMainBinding
    val apiManager = ApiManager()
    lateinit var datanews : ArrayList<Pair<String,String>>
    lateinit var aboutDataMap: MutableMap<String, CoinAboutItem>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.layoutToolbar.toolbar.title = "Market"


        initUi()
        binding.layoutWatchlist.btnShowMore.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.livecoinwatch.com/"))
            startActivity(intent)
        }
        binding.swiperefreshmain.setOnRefreshListener {
            initUi()

            Handler(Looper.getMainLooper()).postDelayed({
                binding.swiperefreshmain.isRefreshing = false
            }, 1500)






        }
        getaboutdatafromassets()
    }

    private fun initUi() {



        getNewaApi()
        getTopCoins()



    }
    private fun getNewaApi() {
        apiManager.getNews(object :ApiManager.ApiCallback<ArrayList<Pair<String,String>>>{
            override fun onSuccess(data: ArrayList<Pair<String, String>>) {
                datanews=data
                refreshnews()

            }

            override fun onError(errorMessage: String) {
                Toast.makeText(this@MainActivity, "you have an error", Toast.LENGTH_SHORT).show()

            }

        })

    }


    private fun getTopCoins(){
        apiManager.getCoinslist(object :ApiManager.ApiCallback<List<CoinData.Data>>{
            override fun onSuccess(data: List<CoinData.Data>) {
                ShowDataRecycler(data)

            }

            override fun onError(errorMessage: String) {
                Toast.makeText(this@MainActivity, "you have an error", Toast.LENGTH_SHORT).show()
            }

        })
    }
    fun refreshnews(){
        val randomAccess=(0..49).random()

        binding.layoutNews.txtNews.text=datanews[randomAccess].first
        binding.layoutNews.txtNews.setOnClickListener {
            refreshnews()
        }
        binding.layoutNews.imgNews.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW,Uri.parse(datanews[randomAccess].second))
            startActivity(intent)

        }


    }
    private fun ShowDataRecycler(data:List<CoinData.Data>){
        val marketAdapter = MarketAdapter(ArrayList(data),this)
        binding.layoutWatchlist.recyclerMain.adapter=marketAdapter
        binding.layoutWatchlist.recyclerMain.layoutManager= LinearLayoutManager(this)



    }




    override fun oncoinitemclicked(dataCoin: CoinData.Data) {
        val intent = Intent(this, CoinActivity::class.java)

        val bundle = Bundle()
        bundle.putParcelable("bundle1", dataCoin)
        bundle.putParcelable("bundle2", aboutDataMap[dataCoin.coinInfo.name])

        intent.putExtra("bundle", bundle)
        startActivity(intent)

    }

    override fun getaboutdatafromassets() {

        val fileInString = applicationContext.assets
            .open("currencyinfo.json")
            .bufferedReader()
            .use { it.readText() }

        aboutDataMap = mutableMapOf<String, CoinAboutItem>()

        val gson = Gson()
        val dataAboutAll = gson.fromJson(fileInString, coinaboutdata::class.java)

        dataAboutAll.forEach {
            aboutDataMap[it.currencyName] = CoinAboutItem(
                it.info.web,
                it.info.github,
                it.info.twt,
                it.info.desc,
                it.info.reddit
            )
        }


    }




}