package com.exness.pushtest.models

/**
 * Created by alexander.shtanko on 12/12/17.
 */

data class QuotesResponse(val base:String?,
                          val date:String,
                          val rates:Map<String,Float>?)
