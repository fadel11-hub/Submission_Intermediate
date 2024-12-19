package com.dicoding.picodiploma.loginwithanimation.view.detailStory

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.response.main.DetailStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.main.ItemStory
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class detailStoryViewModel(application: Application) : AndroidViewModel(application) {

    val detailStory = MutableLiveData<ItemStory>()
    private val errorMessage = MutableLiveData<String>()

    fun setDetailStory(id: String, authorization: String) {
        ApiConfig.apiInstant
            .getDetailStory(id, authorization)
            .enqueue(object : Callback<DetailStoryResponse> {
                override fun onResponse(
                    call: Call<DetailStoryResponse>,
                    response: Response<DetailStoryResponse>
                ) {
                    if (response.isSuccessful) {
                        val story = response.body()?.story
                        if (story != null) {
                            // Map data dari Story ke ItemStory
                            val itemStory = ItemStory(
                                id = story.id ?: "",
                                name = story.name ?: "",
                                description = story.description ?: "",
                                photoUrl = story.photoUrl ?: "",
                                createdAt = story.createdAt ?: ""
                            )
                            detailStory.postValue(itemStory)
                        } else {
                            errorMessage.postValue("Data story tidak tersedia")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        errorMessage.postValue("Gagal memuat data: $errorBody")
                    }
                }

                override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                    errorMessage.postValue("Kesalahan jaringan: ${t.message}")
                }
            })
    }

    fun getDetailStory(): LiveData<ItemStory> {
        return detailStory
    }

    fun getErrorMessage(): LiveData<String> {
        return errorMessage
    }
}
