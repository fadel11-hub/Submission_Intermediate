package com.dicoding.picodiploma.loginwithanimation.view.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiService
import kotlinx.coroutines.launch
import retrofit2.Call

class LoginViewModel(
    private val repository: UserRepository,
    private val apiService: ApiService
) : ViewModel() {

    fun login(email: String, password: String): Call<LoginResponse> {
        return apiService.login(email, password)
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}
