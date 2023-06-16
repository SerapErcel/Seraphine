package com.serapercel.seraphine.model

import java.io.Serializable

data class Music (
    val baseCat: Long? = null,
    val title: String? = null,
    val url: String? = null
) : Serializable
