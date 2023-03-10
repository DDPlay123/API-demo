package com.tutorial.demo.network

import com.tutorial.demo.BuildConfig
import com.tutorial.demo.data.openAI.ChatGPTReq
import com.tutorial.demo.data.openAI.ChatGPTRes
import com.tutorial.demo.data.placesAutoComplete.AutoComplete
import com.tutorial.demo.data.placesDetails.PlacesDetails
import com.tutorial.demo.data.placesSearch.PlacesSearch
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @author 麥光廷
 * @date 2023-03-10
 */
interface ApiService {

    /**
     * Chat GPT API
     */
    @Headers("Accept-Encoding: identity","Content-Type: application/json","Authorization: Bearer ${BuildConfig.CHAT_GPT_KEY}")
    @POST("completions")
    fun sendChatGPT(@Body body: com.tutorial.demo.data.openAI.ChatGPTReq): Call<com.tutorial.demo.data.openAI.ChatGPTRes>

    /**
     * Google Places Auto Complete
     */
    @Headers("Accept-Encoding: identity")
    @GET("autocomplete/json")
    fun getPlacesAutoComplete(
        @Query("input") input: String,
        @Query("location") location: String, // Ex: 25.0338,121.5646
        @Query("components") component: String = "country:tw",
        @Query("radius") radius: String = "10000",
        @Query("type") type: String = "restaurant",
        @Query("key") key: String,
        @Query("language") language: String = "zh-TW"
    ): Call<com.tutorial.demo.data.placesAutoComplete.AutoComplete>

    /**
     * Google Places Search
     */
    @Headers("Accept-Encoding: identity")
    @GET("nearbysearch/json")
    fun getPlaceSearch(
        @Query("location") location: String, // Ex: 25.0338,121.5646
        @Query("radius") radius: String = "1000", // Ex: 1000 公尺
        @Query("type") type: String = "restaurant",
        @Query("key") key: String,
        @Query("language") language: String = "zh-TW"
    ): Call<com.tutorial.demo.data.placesSearch.PlacesSearch>

    @Headers("Accept-Encoding: identity")
    @GET("nearbysearch/json")
    fun getPlaceSearch(
        @Query("location") location: String, // Ex: 25.0338,121.5646
        @Query("radius") radius: String = "1000", // Ex: 1000 公尺
        @Query("pagetoken") token: String,
        @Query("type") type: String = "restaurant",
        @Query("key") key: String,
        @Query("language") language: String = "zh-TW"
    ): Call<com.tutorial.demo.data.placesSearch.PlacesSearch>

    /**
     * Google Places Search with keyword
     */
    @Headers("Accept-Encoding: identity")
    @GET("nearbysearch/json")
    fun getPlaceSearchWithKeyword(
        @Query("location") location: String, // Ex: 25.0338,121.5646
        @Query("radius") radius: Long = 1000L, // Ex: 1000 公尺
        @Query("keyword") keyword: String,
        @Query("key") key: String,
        @Query("language") language: String = "zh-TW"
    ): Call<com.tutorial.demo.data.placesSearch.PlacesSearch>

    @Headers("Accept-Encoding: identity")
    @GET("nearbysearch/json")
    fun getPlaceSearchWithKeyword(
        @Query("location") location: String, // Ex: 25.0338,121.5646
        @Query("radius") radius: Long = 1000L, // Ex: 1000 公尺
        @Query("pagetoken") token: String,
        @Query("keyword") keyword: String,
        @Query("key") key: String,
        @Query("language") language: String = "zh-TW"
    ): Call<com.tutorial.demo.data.placesSearch.PlacesSearch>

    /**
     * Google Places Details
     */
    @Headers("Accept-Encoding: identity")
    @GET("details/json")
    fun getPlaceDetails(
        @Query("place_id") placeID: String,
        @Query("language") language: String = "zh-TW",
        @Query("key") key: String
    ): Call<com.tutorial.demo.data.placesDetails.PlacesDetails>
}