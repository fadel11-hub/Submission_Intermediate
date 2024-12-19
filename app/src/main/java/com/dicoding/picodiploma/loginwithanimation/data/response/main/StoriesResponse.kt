package com.dicoding.picodiploma.loginwithanimation.data.response.main

import com.google.gson.annotations.SerializedName

data class StoriesResponse(

	@field:SerializedName("listStory")
	val items : ArrayList<ItemStory>,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)
