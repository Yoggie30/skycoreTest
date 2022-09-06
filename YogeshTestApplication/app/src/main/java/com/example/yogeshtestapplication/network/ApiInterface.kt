package com.example.yogeshtestapplication.network

import com.example.yogeshtestapplication.model.ResponseDO
import io.reactivex.Observable
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.QueryMap

interface ApiInterface {

    @GET("search")
    fun search(@Header("Authorization") h1:String, @QueryMap objMap: HashMap<String, Any>): Observable<ResponseDO>

}