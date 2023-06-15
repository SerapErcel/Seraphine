package com.serapercel.seraphine.service

import com.serapercel.seraphine.model.MusicResponse
import retrofit2.http.GET
import retrofit2.Call

interface MusicService {

    @GET("f27fbefc-d775-4aee-8d65-30f76f1f7109")
    fun allMusics(): Call<MusicResponse>
}