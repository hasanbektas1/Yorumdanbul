package com.hasanbektas.yorumdanbull.model

import com.google.gson.annotations.SerializedName

data class YorumdanbulCommentDataModel (

    @SerializedName("content")
    val comment : String,

    @SerializedName("createdAt")
    val commenCreatedDate : String

) : java.io.Serializable