package kr.hs.dgsw.nonabilryo

import com.google.gson.annotations.SerializedName

data class SignupResponse(
    @SerializedName("state") val state: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: SignupData
) {
    data class SignupData(
        @SerializedName("accessToken") val accessToken: String,
        @SerializedName("refreshToken") val refreshToken: String
    )
}