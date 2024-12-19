package com.dicoding.picodiploma.loginwithanimation.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.response.main.ItemStory
import com.dicoding.picodiploma.loginwithanimation.data.response.main.StoriesResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val listStories = MutableLiveData<ArrayList<ItemStory>>()
    private val errorMessage = MutableLiveData<String>()

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getAllStories(authorization: String) {
        ApiConfig.apiInstant
            .getAllStories(authorization)
            .enqueue(object : Callback<StoriesResponse> {
                override fun onResponse(
                    call: Call<StoriesResponse>,
                    response: Response<StoriesResponse>
                ) {
                    if (response.isSuccessful) {
                        listStories.postValue(response.body()?.items)
                    } else {
                        val errorBody = response.errorBody()?.string()
                        errorMessage.postValue("Error $errorBody")
                    }
                }
                override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                    t.message?.let {
                        errorMessage.postValue(R.string.gagal_memuat_data.toString())}
                }
            })
    }


    fun getListStories(): LiveData<ArrayList<ItemStory>> {
        return listStories
    }
}
