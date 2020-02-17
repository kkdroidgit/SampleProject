package com.example.android.sampleproject.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BlogModel(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("body") val body: List<Bloggers>
)

data class Bloggers(
    @SerializedName("imgUrl") val image: String,
    @SerializedName("blogName") val title: String,
    @SerializedName("blogUrl") val blogUrl: String,
    @SerializedName("about") val about: String
) : Serializable