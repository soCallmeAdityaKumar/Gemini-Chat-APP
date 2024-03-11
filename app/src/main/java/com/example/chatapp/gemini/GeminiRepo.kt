package com.example.chatapp.gemini

import android.util.Log
import com.google.ai.client.generativeai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel

class GeminiRepo {
    private var response:String?=null
    private val API_KEY=""
    private var generativeModel:GenerativeModel?=null
    init {
        generativeModel = GenerativeModel(
            // Use a model that's applicable for your use case (see "Implement basic use cases" below)
            modelName = "gemini-pro",
            // Access your API key as a Build Configuration variable (see "Set up your API key" above)
            apiKey = API_KEY
        )
    }



    suspend fun sendInput(text:String){
        Log.d("gemini->sendInput->",text)
        response= try{generativeModel?.generateContent(text)?.text}catch (e:Exception){
             null
        }
    }
    fun getResponse(): String? {
        return response
    }

}