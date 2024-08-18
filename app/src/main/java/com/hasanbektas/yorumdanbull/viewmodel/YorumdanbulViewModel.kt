package com.hasanbektas.yorumdanbull.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hasanbektas.yorumdanbull.model.YorumdanbulCategoryModel
import com.hasanbektas.yorumdanbull.model.YorumdanbulModel
import com.hasanbektas.yorumdanbull.service.YorumdanbulService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class YorumdanbulViewModel:ViewModel() {

    private val ybAPIservice = YorumdanbulService()

    val prodcutList = MutableLiveData<ArrayList<YorumdanbulModel>>()
    val categoryList = MutableLiveData<List<YorumdanbulCategoryModel>>()
    val ybError = MutableLiveData<Boolean>()
    val ybLoading = MutableLiveData<Boolean>()


    fun getProductDataFromAPI(searchString: String,searchId: Int,searcCategory: String) {

        ybLoading.value = true

        val call = ybAPIservice.getProductSearchService(searchString,searchId,searcCategory)

        call.enqueue(object : Callback<ArrayList<YorumdanbulModel>> {
            override fun onResponse(call: Call<ArrayList<YorumdanbulModel>>, response: Response<ArrayList<YorumdanbulModel>>) {
                if (response.isSuccessful){
                    response.body()?.let {

                        ybError.value = false
                        ybLoading.value = false

                        prodcutList.value = it
                    }
                }
            }
            override fun onFailure(call: Call<ArrayList<YorumdanbulModel>>, t: Throwable) {
                ybError.value = true
                ybLoading.value = false
                t.printStackTrace()
            }
        })
    }
    fun getCategoryDataFromAPI(){

        val call = ybAPIservice.getCategoryFilterService()

        call.enqueue(object : Callback<List<YorumdanbulCategoryModel>> {
            override fun onResponse(
                call: Call<List<YorumdanbulCategoryModel>>,
                response: Response<List<YorumdanbulCategoryModel>>,
            ) {
                if (response.isSuccessful){
                    response.body()?.let {
                        categoryList.value = it
                    }
                }
            }
            override fun onFailure(call: Call<List<YorumdanbulCategoryModel>>, t: Throwable) {
                ybLoading.value = false
                ybError.value = true
                t.printStackTrace()
            }
        })
    }
}