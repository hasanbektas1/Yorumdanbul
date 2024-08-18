package com.hasanbektas.yorumdanbull.model

import com.google.gson.annotations.SerializedName

class YorumdanbulCategoryModel (

    @SerializedName("category_name")
    val categoryName : String,

    @SerializedName("category_id")
    val categoryId : String
)