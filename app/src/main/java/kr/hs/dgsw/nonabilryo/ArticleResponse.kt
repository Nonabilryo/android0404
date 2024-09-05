package kr.hs.dgsw.nonabilryo

import com.google.gson.annotations.SerializedName

data class ArticleResponse(
    @SerializedName("state") val state: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Data
) {
    data class Data(
        @SerializedName("content") val content: List<Article>,
        @SerializedName("pageable") val pageable: Pageable,
        @SerializedName("last") val last: Boolean,
        @SerializedName("totalElements") val totalElements: Int,
        @SerializedName("totalPages") val totalPages: Int,
        @SerializedName("number") val number: Int,
        @SerializedName("first") val first: Boolean,
        @SerializedName("sort") val sort: Sort,
        @SerializedName("size") val size: Int,
        @SerializedName("numberOfElements") val numberOfElements: Int,
        @SerializedName("empty") val empty: Boolean
    ) {
        data class Article(
            @SerializedName("title") val title: String,
            @SerializedName("price") val price: Long,
            @SerializedName("rentalType") val rentalType: String,
            @SerializedName("image") val image: Image,
            @SerializedName("createdAt") val createdAt: String
        )

        data class Image(
            @SerializedName("idx") val idx: String,
            @SerializedName("url") val url: String
        )

        data class Pageable(
            @SerializedName("pageNumber") val pageNumber: Int,
            @SerializedName("pageSize") val pageSize: Int,
            @SerializedName("sort") val sort: Sort,
            @SerializedName("offset") val offset: Int,
            @SerializedName("paged") val paged: Boolean,
            @SerializedName("unpaged") val unpaged: Boolean
        )

        data class Sort(
            @SerializedName("empty") val empty: Boolean,
            @SerializedName("unsorted") val unsorted: Boolean,
            @SerializedName("sorted") val sorted: Boolean
        )
    }
}