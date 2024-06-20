package kr.hs.dgsw.nonabilryo

import com.google.gson.annotations.SerializedName

data class SignupResponse(
    @SerializedName("state") val state: Int,
    @SerializedName("message") val message: String,
)