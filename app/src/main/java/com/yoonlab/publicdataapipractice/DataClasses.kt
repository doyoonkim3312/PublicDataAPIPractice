package com.yoonlab.publicdataapipractice

// Making a containers to hold the data from HTTP response.

import okhttp3.Response
import okhttp3.internal.http2.Header
import retrofit2.http.Body
    data class WEATHER(val response: RESPONSE)

    data class RESPONSE(val header: HEADER, val body: BODY)

    data class HEADER(val resultCode: Int, val resultMsg: String)

    data class BODY(val dataType: String, val items: ITEMS)

    data class ITEMS(val item: List<ITEM>)

    data class ITEM(val baseDate: Int, val baseTime: Int, val category: String, val fcstValue: Double) {
    }






