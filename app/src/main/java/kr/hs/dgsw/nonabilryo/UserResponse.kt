package kr.hs.dgsw.nonabilryo

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("state") val state: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: UserData
)

data class UserData(
    @SerializedName("idx") val idx: String,
    @SerializedName("name") val name: String,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("description") val description: String,
    @SerializedName("signed") val signed: String
)