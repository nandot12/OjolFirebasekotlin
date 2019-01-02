package com.udacoding.ojolfirebasekotlin.network

import com.udacoding.ojolfirebasekotlin.utama.home.model.ResultRoute
import io.reactivex.Flowable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @GET("json")
    fun actionRoute(@Query("origin") origin : String,
                    @Query("destination")destinations : String,
                    @Query("key")key : String):Flowable<ResultRoute>


    @Headers(
        "Authorization: key=AAAAaxE9ACM:APA91bEwpI1_X12xvGzRsvQiWwJ6HErQcCM12O4i2K1CypFfO2gGaIBMASoMbv-fYzv5o8UA-krw43pZIiCfOI80Iu_GgEsU9OdtpQWHzvYFqlvE7MXIzVpb905tBXAPgT0Srj0qjZYz",
        "Content-Type:application/json"
    )
    @POST("fcm/send")
    fun sendChatNotification(@Body requestNotificaton: RequestNotificaton): Call<ResponseBody>
}