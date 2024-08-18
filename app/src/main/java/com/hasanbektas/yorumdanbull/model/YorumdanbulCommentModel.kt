package com.hasanbektas.yorumdanbull.model

import com.google.gson.annotations.SerializedName

class YorumdanbulCommentModel (
    @SerializedName("data")
    val commentData : List<YorumdanbulCommentDataModel>

): java.io.Serializable