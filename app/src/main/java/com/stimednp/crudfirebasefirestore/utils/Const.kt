package com.stimednp.crudfirebasefirestore.utils

/**
 * Created by rivaldy on 12/25/2019.
 */

object Const {
    val PATH_COLLECTION = "users"
    val PATH_AGE = "intAge"

    fun setTimeStamp(): Long {
        val time = (-1 * System.currentTimeMillis())
        return time
    }
}