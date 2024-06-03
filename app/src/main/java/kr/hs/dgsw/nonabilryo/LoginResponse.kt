package kr.hs.dgsw.nonabilryo

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("state") val state: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: LoginData
) {
    data class LoginData(
        @SerializedName("accessToken") val accessToken: String,
        @SerializedName("refreshToken") val refreshToken: String
    )
}