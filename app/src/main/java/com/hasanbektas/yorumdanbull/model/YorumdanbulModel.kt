package com.hasanbektas.yorumdanbull.model

import com.google.gson.annotations.SerializedName

class YorumdanbulModel (

    @SerializedName("total")
    val commentTotal: String,

    @SerializedName("category")
    val productCategory: String,

    @SerializedName("category_id")
    val productCategoryId: String,

    @SerializedName("image_url")
    val productImageUrl : String,

    @SerializedName("name")
    val productName : String,

    @SerializedName("rating")
    val productRating : String,

    @SerializedName("sku_id")
    val productSkuId : String,

    @SerializedName("term_content_list")
    val comment_content_list : YorumdanbulCommentModel
) : java.io.Serializable