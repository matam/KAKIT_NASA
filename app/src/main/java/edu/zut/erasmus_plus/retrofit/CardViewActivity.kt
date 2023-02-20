package edu.zut.erasmus_plus.retrofit

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import java.util.ArrayList

class CardViewActivity: AppCompatActivity() {

    private var data = ArrayList<AstronomyPictureDayEntity>()
    private val adapter = ApodsRecyclerViewAdapter(data)

    private var current = LocalDateTime.now()
    private var isLoading = false

    private var startDate:Long=10
    private var endDate:Long=0

    lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.android_card_view_layout)

        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)
        layoutManager= LinearLayoutManager(this)
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.adapter = adapter

        loadItems(startDate,endDate)


        recyclerview.addOnScrollListener(object: RecyclerView.OnScrollListener()
        {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isLoading)
                {
                    if(!recyclerView.canScrollVertically(1))
                    {
                        isLoading=true
                        endDate=startDate+1
                        startDate+=4

                        loadItems(startDate,endDate)
                    }

                }
            }

        }
        )
    }

    private fun loadItems(daysStart: Long, daysEnd: Long)
    {
        if (isNetworkConnected()) {
            getApodList(reformatDate(daysStart),reformatDate(daysEnd))
        }
    }
    private fun getApodList(startDate: String, endDate: String){
        val service = ApodService.getApodItems()
        val serviceRequest = service.getApodStartEndDate(ApodService.API_KEY, startDate,endDate)
        serviceRequest.enqueue(object: Callback<List<AstronomyPictureDayEntity>>
        {
            override fun onResponse(
                call: Call<List<AstronomyPictureDayEntity>>,
                response: Response<List<AstronomyPictureDayEntity>>
            ) {
                response.body()?.forEach { data.add (it)  }
                data.sortByDescending { list->list.date }
                adapter.notifyDataSetChanged()
                isLoading=false

            }

            override fun onFailure(call: Call<List<AstronomyPictureDayEntity>>, t: Throwable) {
                Log.i(CardViewActivity::class.simpleName, "on FAILURE!!!!")
            }

        })

    }
    private fun reformatDate(valueDays: Long) : String
    {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        current = LocalDateTime.now().minusDays(valueDays)
        return current.format(formatter)

    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
        return (networkCapabilities != null) && networkCapabilities.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_INTERNET
        )
    }

}


