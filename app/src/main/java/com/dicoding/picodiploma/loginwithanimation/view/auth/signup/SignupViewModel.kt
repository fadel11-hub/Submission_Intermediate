package com.dicoding.picodiploma.loginwithanimation.view.auth.signup

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.response.SignupResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiService
import retrofit2.Call

class SignupViewModel (
    private val apiService: ApiService
    ) : ViewModel() {

        fun register (name: String, email: String, password: String): Call<SignupResponse> {
            return apiService.register(name, email, password)
        }
    }

