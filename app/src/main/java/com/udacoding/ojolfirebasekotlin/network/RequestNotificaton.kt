package com.udacoding.ojolfirebasekotlin.network

import com.google.gson.annotations.SerializedName
import com.udacoding.ojolfirebasekotlin.utama.home.model.Booking


class RequestNotificaton {

    @SerializedName("to")
    var token: String? = null

    @SerializedName("notification")
    var sendNotificationModel: Booking? = null
}