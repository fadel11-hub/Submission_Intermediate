package com.dicoding.picodiploma.loginwithanimation.data.retrofit

import com.dicoding.picodiploma.loginwithanimation.data.response.main.AddNewStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.main.DetailStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.SignupResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.main.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<SignupResponse>

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") authorization: String
    ): Call<StoriesResponse>

    @GET("stories/{id}")
    fun getDetailStory(
        @Path("id") id: String,
        @Header("Authorization") authorization: String,
    ): Call<DetailStoryResponse>

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") authorization: String,
    ): AddNewStoryResponse
}
