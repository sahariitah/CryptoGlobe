package ir.dunijet.dunipool.apiManager



import com.example.dunipool.apiManager.ALL
import com.example.dunipool.apiManager.HISTO_DAY
import com.example.dunipool.apiManager.HISTO_HOUR
import com.example.dunipool.apiManager.HISTO_MINUTE
import com.example.dunipool.apiManager.HOUR
import com.example.dunipool.apiManager.HOURS24
import com.example.dunipool.apiManager.MONTH
import com.example.dunipool.apiManager.MONTH3
import com.example.dunipool.apiManager.WEEK
import com.example.dunipool.apiManager.YEAR
import com.example.dunipool.apiManager.model.ChartData
import ir.dunijet.dunipool.apiManager.model.CoinData
import ir.dunijet.dunipool.apiManager.model.NewsData
import kotlinx.coroutines.flow.callbackFlow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://min-api.cryptocompare.com/data/"
const val BASE_URL_IMAGE = "https://www.cryptocompare.com"
const val API_KEY = "authorization: Apikey 98ac91fe0448e6e73045b9ac770b319602951c730179f0dee37ed6d3c58b767f"
const val APP_NAME = "Test app"

class ApiManager {
    private val apiService: ApiService

    init {

        val retrofit = Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

    }

    fun getNews(apiCallback: ApiCallback<ArrayList<Pair<String, String>>>) {
        apiService.getTopNews().enqueue(object : Callback<NewsData> {
            override fun onResponse(call: Call<NewsData>, response: Response<NewsData>) {
                val data = response.body()!!

                val dataToSendData: ArrayList<Pair<String, String>> = arrayListOf()
                data.data.forEach {
                    dataToSendData.add(Pair(it.title, it.url))
                }
                apiCallback.onSuccess(dataToSendData)


            }

            override fun onFailure(call: Call<NewsData>, t: Throwable) {
                apiCallback.onError(t.message!!)

            }

        })
    }

    fun getCoinslist(apiCallback: ApiCallback<List<CoinData.Data>>) {
        apiService.getTopCoins().enqueue(object : Callback<CoinData> {
            override fun onResponse(call: Call<CoinData>, response: Response<CoinData>) {
                val data = response.body()!!
                apiCallback.onSuccess(data.data)


            }

            override fun onFailure(call: Call<CoinData>, t: Throwable) {
                apiCallback.onError(t.message!!)

            }

        })


    }

    fun getChartData(
        symbol: String,
        period: String,
        apiCallback: ApiCallback<Pair<List<ChartData.Data>, ChartData.Data?>>
    ) {

        var histoPeriod = ""
        var limit = 30
        var aggregate = 1




        when (period) {

            HOUR -> {
                histoPeriod = HISTO_MINUTE
                limit = 60
                aggregate = 12
            }

            HOURS24 -> {
                histoPeriod = HISTO_HOUR
                limit = 24
            }

            MONTH -> {
                histoPeriod = HISTO_DAY
                limit = 30
            }

            MONTH3 -> {
                histoPeriod = HISTO_DAY
                limit = 90
            }

            WEEK -> {
                histoPeriod = HISTO_HOUR
                aggregate = 6
            }

            YEAR -> {
                histoPeriod = HISTO_DAY
                aggregate = 13
            }

            ALL -> {
                histoPeriod = HISTO_DAY
                aggregate = 30
                limit = 2000
            }
        }



        apiService.getChartData(histoPeriod, symbol, limit, aggregate)
            .enqueue(object : Callback<ChartData> {
                override fun onResponse(call: Call<ChartData>, response: Response<ChartData>) {

                    val dataFull = response.body()!!
                    val data1 = dataFull.data
                    val data2 = dataFull.data.maxByOrNull { it.close.toFloat() }
                    val returningData = Pair(data1, data2)

                    apiCallback.onSuccess(returningData)

                }

                override fun onFailure(call: Call<ChartData>, t: Throwable) {
                    apiCallback.onError(t.message!!)
                }

            })

    }


    interface ApiCallback<T> {

        fun onSuccess(data: T)
        fun onError(errorMessage: String)

    }
}

