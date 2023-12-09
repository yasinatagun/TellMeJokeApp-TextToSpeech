package com.example.jokeapp.model

import com.google.gson.annotations.SerializedName

data class JokeModel (
    @SerializedName("setup")
    val setup: String,

    @SerializedName("delivery")
    val delivery: String
    )