package com.melegy.retrofitcoroutines

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Success(

    @field:JsonProperty("activity")
    val activity: String? = null,


    @field:JsonProperty("link")
    val link: String? = null,


    @field:JsonProperty("price")
    val price: Double? = null,

    @field:JsonProperty("error")
    val error: String? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Error(

    @field:JsonProperty("error")
    val error: String? = null

)
