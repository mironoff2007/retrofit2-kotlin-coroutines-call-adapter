package com.melegy.retrofitcoroutines

import com.google.gson.annotations.SerializedName


data class Fact(

    @SerializedName("fact")
    val fact: String? = null,

    @SerializedName("length")
    val length: Int? = null,

    @SerializedName("error")
    val error: String? = null
)

data class Error(

    @SerializedName("code")
    val error: String? = null,

    @SerializedName("message")
    val message: String? = null

)
