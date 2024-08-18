package com.hasanbektas.yorumdanbull.service

import com.hasanbektas.yorumdanbull.model.YorumdanbulCategoryModel
import com.hasanbektas.yorumdanbull.model.YorumdanbulModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class YorumdanbulService {

    val BASE_URL = "*********"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(IYorumdanbulAPI::class.java)

    fun getProductSearchService(searchText: String,pageNumber: Int,categoryId: String): Call<ArrayList<YorumdanbulModel>> {

        return retrofit.getProductSearchData(searchText,pageNumber,categoryId)

    }

    fun getCategoryFilterService(): Call<List<YorumdanbulCategoryModel>> {

        return retrofit.getCategoryFilterData()
    }

}