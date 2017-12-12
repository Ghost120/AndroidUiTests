package com.exness.pushtest.models

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Created by alexander.shtanko on 12/12/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class  QuotesResponse @JsonCreator constructor(@JsonProperty("base") val base:String?,
                                                    @JsonProperty("date") val date:String,
                                                    @JsonProperty("rates") val rates:Map<String,Float>?)
