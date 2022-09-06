package com.example.yogeshtestapplication.repository

import android.content.Context
import com.example.yogeshtestapplication.network.ApiInterface
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

open class BaseRepository {

    private var service: ApiInterface? = null
    lateinit var retrofit: Retrofit
    private var baseUrl = "https://api.yelp.com/v3/businesses/"

    fun getNetworkService(context: Context): ApiInterface {
        if (service == null) {
            service = getService(context)
        }
        return service!!
    }

    private fun getService(context: Context): ApiInterface {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient: OkHttpClient =
            OkHttpClient.Builder()
                .connectionSpecs(
                    listOf(
                        ConnectionSpec.MODERN_TLS,
                        ConnectionSpec.CLEARTEXT
                    )
                )
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build()

        // Retrofit handling
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        return retrofit.create(ApiInterface::class.java)
    }
}