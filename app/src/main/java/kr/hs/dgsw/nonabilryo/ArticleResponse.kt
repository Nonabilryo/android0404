package kr.hs.dgsw.nonabilryo

import com.google.gson.annotations.SerializedName

data class ArticleResponse(
    @SerializedName("state") val state: Int,
    @SerializedName("message") val message: String
)