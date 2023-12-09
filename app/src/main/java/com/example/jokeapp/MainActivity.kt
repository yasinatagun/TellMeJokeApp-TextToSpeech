package com.example.jokeapp

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import com.example.jokeapp.databinding.ActivityMainBinding
import com.example.jokeapp.model.JokeModel
import com.example.jokeapp.service.JokeAPI
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var tts: TextToSpeech? = null
    private lateinit var binding: ActivityMainBinding
    var jokeQuestion = ""
    var jokeAnswer = ""
    private val BASE_URL = "https://v2.jokeapi.dev/"
    private var jokeModelList: ArrayList<JokeModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        println(Locale.getDefault().language.toString() + "ajushduahsdusahdu")

        loadData()
        showJoke()
        tts = TextToSpeech(this, this)

        binding.btnSpeaker.setOnClickListener { speakOut() }
        binding.btnRandomJoke.setOnClickListener{ createNewJoke()}
    }

    private fun createNewJoke() {
        loadData()
        showJoke()
    }
    private fun speakOut() {
        val text = binding.txtJokeQuestion.text.toString() + "                  " + binding.txtJokeAnswer.text.toString()
        println(text + "asdasdsadad")
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
    }
    private fun showJoke() {
        binding.txtJokeQuestion.text = jokeQuestion
        binding.txtJokeAnswer.text = jokeAnswer
    }
    public override fun onDestroy() {
        // Shutdown TTS when
        // activity is destroyed
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
    private fun loadData() {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(JokeAPI::class.java)
        val call = service.getData()
        call.enqueue(object : Callback<JokeModel> {
            override fun onResponse(
                call: Call<JokeModel>,
                response: Response<JokeModel>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        var jokeModel: JokeModel = it
                        jokeQuestion = it.setup
                        jokeAnswer = it.delivery
                        println(jokeQuestion)
                        println(jokeAnswer)
                        showJoke()
                    }

                }
            }

            override fun onFailure(call: Call<JokeModel>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })


    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language not supported!")
            } else {
                binding.btnSpeaker.isEnabled = true
            }
        }
    }


}