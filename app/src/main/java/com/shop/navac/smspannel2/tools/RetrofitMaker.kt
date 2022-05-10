package com.shop.navac.smspannel2.tools

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitMaker {
    //TODO: check if we should make this Singleton
    fun getInstance(): Retrofit {
        val gson = GsonBuilder()
            //                .registerTypeAdapter(Id.class, new IdTypeAdapter())
                //                .enableComplexMapKeySerialization()
                //                .serializeNulls()
                //                .setDateFormat(DateFormat.LONG)
                //                .setPrettyPrinting()
                //                .setVersion(1.0)
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
        val httpClient = OkHttpClient.Builder()

        //default timeout is 10s
        httpClient.connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
        val client = httpClient.build()
        return Retrofit.Builder().baseUrl(Settings.baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            // we need to add converter factory to
            // convert JSON object to Java object
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }
}