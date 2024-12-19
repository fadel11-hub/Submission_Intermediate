package com.dicoding.picodiploma.loginwithanimation.view.auth.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import kotlinx.coroutines.launch

class SettingViewModel (private val repository: UserRepository) : ViewModel() {

    private val _logoutSuccess = MutableLiveData<Boolean>()
    val logoutSuccess: LiveData<Boolean>
        get() = _logoutSuccess

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _logoutSuccess.postValue(true)
        }
    }
}