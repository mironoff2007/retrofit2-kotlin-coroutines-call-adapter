package com.melegy.retrofitcoroutines

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Success(

    @Json(name = "activity")
    val activity: String? = null,


    @Json(name = "link")
    val link: String? = null,


    @Json(name = "price")
    val price: Double? = null,

    @Json(name = "error")
    val error: String? = null
)

@JsonClass(generateAdapter = true)
data class Error(

    @Json(name = "error")
    val error: String? = null

)
