package com.hasanbektas.yorumdanbull.service

import com.hasanbektas.yorumdanbull.model.YorumdanbulCategoryModel
import com.hasanbektas.yorumdanbull.model.YorumdanbulModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IYorumdanbulAPI {

    @GET("*****")
    fun getCategoryFilterData () : Call<List<YorumdanbulCategoryModel>>

    @GET("********/{search}/{id}/{category}")
    fun getProductSearchData(@Path("search") search : String,
                             @Path("id") id : Int,
                             @Path("category") category : String) : Call<ArrayList<YorumdanbulModel>>
}