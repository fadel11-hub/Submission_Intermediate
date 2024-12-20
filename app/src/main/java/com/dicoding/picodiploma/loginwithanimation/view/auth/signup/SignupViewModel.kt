package com.dicoding.picodiploma.loginwithanimation.view.auth.signup

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.response.SignupResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiService
import retrofit2.Call

class SignupViewModel(
    private val repository: UserRepository,
    private val apiService: ApiService
) : ViewModel() {

    fun register(email: String, password: String, name: String) = apiService.register(email, password, name)
}


