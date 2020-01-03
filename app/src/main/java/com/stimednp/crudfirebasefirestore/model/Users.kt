package com.stimednp.crudfirebasefirestore.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by rivaldy on 12/25/2019.
 */

@Parcelize
data class Users(
    var strId: String = "0",
    var strName: String? = null,
    var strAddress: String? = null,
    var intAge: Int? = 0
) : Parcelable