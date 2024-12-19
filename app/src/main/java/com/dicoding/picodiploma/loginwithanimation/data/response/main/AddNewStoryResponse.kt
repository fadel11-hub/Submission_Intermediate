package com.dicoding.picodiploma.loginwithanimation.data.response.main

import com.google.gson.annotations.SerializedName

data class AddNewStoryResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
