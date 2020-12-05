package com.yoonlab.publicdataapipractice

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*

private val retrofit = Retrofit.Builder()
    .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startRetrofitConnection(retrofit, this)

        val refreshBtn = findViewById<Button>(R.id.btn_refresh)
        refreshBtn.setOnClickListener {
            Toast.makeText(this, "REFRESH", Toast.LENGTH_LONG).show()
            startRetrofitConnection(retrofit,this)
        }


    }

    fun callRetrofitService(retrofit: Retrofit) : WeatherInterface {
        //initializing and start Retrofit service
        return retrofit.create(WeatherInterface::class.java)
    }

    fun getCurrntDate() : String{
        lateinit var currentDate : String
        val calendarInstance = Calendar.getInstance()
        var year = calendarInstance.get(Calendar.YEAR).toString()

        var month = (calendarInstance.get(Calendar.MONTH) + 1).toString()
        if(month.toInt() < 10) month = "0$month"

        var day = calendarInstance.get(Calendar.DATE).toString()
        if(day.toInt() < 10) day = "0$day"

        currentDate = year + month + day

        println(currentDate)

        return currentDate
    }

    fun startRetrofitConnection(retrofit: Retrofit, context : Context) {

        val lowestTemp = findViewById<TextView>(R.id.tmnValue)    //lowest Temp
        val highestTemp = findViewById<TextView>(R.id.tmxValue)    //highest Temp
        val rainPossibility = findViewById<TextView>(R.id.popValue)
        val rainType = findViewById<TextView>(R.id.ptyValue)
        val humidity = findViewById<TextView>(R.id.rehValue)
        val weatherImage = findViewById<ImageView>(R.id.weatherImage)
        val currentTemp = findViewById<TextView>(R.id.currentTemp)


        val retrofitCall = callRetrofitService(retrofit).getWeather(
                "JSON", 10, 1, getCurrntDate().toInt(), getBaseTime(), "55", "127"
        )
        retrofitCall.enqueue(object : retrofit2.Callback<WEATHER>{
            override fun onResponse(call: Call<WEATHER>, response: Response<WEATHER>) {
                if (response.isSuccessful) {
                    Log.d("api", response.body().toString())
                    val itemList = response.body()?.response?.body?.items?.item
                    itemList?.forEach { it ->
                        when (it.category) {
                            "POP" -> rainPossibility.text = it.fcstValue.toString() + "%"
                            "PTY" -> rainType.text = rainTypeDetector(it.fcstValue)
                            "REH" -> humidity.text = it.fcstValue.toString() + "%"
                            "SKY" -> if (it.fcstValue <= 3) weatherImage.setImageResource(
                                    R.drawable.ic_baseline_wb_sunny_24
                            ) else weatherImage.setImageResource(
                                    R.drawable.ic_baseline_wb_cloudy_24
                            )
                            "TMN" -> lowestTemp.text = it.fcstValue.toString()
                            "TMX" -> highestTemp.text = it.fcstValue.toString()
                            "T3H" -> currentTemp.text = it.fcstValue.toString()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<WEATHER>, t: Throwable) {
                Log.d("api faild", t.message.toString())
                Toast.makeText(context, "Connection Faild", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun rainTypeDetector(returnValue : Double) : String {
        var result : String = " "    //initializing default value
        when (returnValue) {
            0.0 -> result =  " "
            1.0 -> result =  "비"
            2.0 -> result =  "비/눈"
            3.0 -> result =  "눈"
            4.0 -> result =  "소나기"
            5.0 -> result =  "빗방울"
            6.0 -> result =  "빗방울/눈날림"
            7.0 -> result =  "눈날림"
        }
        return result
    }

    fun getBaseTime() : String {

        val calendarInstance = Calendar.getInstance()
        val date = SimpleDateFormat("HHmm").format(calendarInstance.time).toInt()

        if (date in 0..200) {
            return "0000"
        } else if (date in 200..499) {
            return "0200"
        } else if (date in 500..799) {
            return "0500"
        } else if (date in 800..1099) {
            return "0800"
        } else if (date in 1100..1399) {
            return "1100"
        } else if (date in 1400..1699) {
            return "1400"
        } else if (date in 1700..1999) {
            return "1700"
        } else if (date in 2000..2299) {
            return "2000"
        } else {
            return "2300"
        }

    }
}

