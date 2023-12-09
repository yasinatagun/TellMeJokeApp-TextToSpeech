package com.example.jokeapp.service

import android.database.Observable
import com.example.jokeapp.model.JokeModel
import retrofit2.Call
import retrofit2.http.GET

interface JokeAPI {
    @GET("joke/Any")
    fun getData(): Call<JokeModel>
}