package com.yoonlab.publicdataapipractice

import android.renderscript.Element
import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.security.Key

val numOfRows = 1
val pageNo = 1
val dataType = "JSON"
val baseDate = 20201128
val baseTime = 1100
val nx = "1"
val ny = "1"

interface WeatherInterface {
    @GET("getVilageFcst?serviceKey=iFMBgo2Vxtc4kr%2F4PsNzJpSfZfOPgjPDRW0wwRMF5StuXk5ygDiJYxcG1Wu5QBLQQ550EkjyBsw69ATLEfbKRA%3D%3D")
    fun getWeather(@Query("dataType")dataType: String, //this means ...&dataType=dataType&... inside of URL
                   @Query("numOfRows")numOfRows: Int,
                   @Query("pageNo")pageNo: Int,
                   @Query("base_date")baseDate: Int,
                   @Query("base_time")baseTime: String,
                   @Query("nx")nx: String,
                   @Query("ny")ny: String): Call<WEATHER>
}