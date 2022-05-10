package com.shop.navac.smspannel2.services

import com.shop.navac.smspannel2.retrofit_models.SMS
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


public interface smsPanelService {
    @GET("Utils/sms_panel/get_first_sms/__all__")
    fun getLpopSMS() : Call<SMS?>
}