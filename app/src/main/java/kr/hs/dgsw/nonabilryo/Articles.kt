package com.findfree.android.ui.home

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Articles {
    @SerializedName("state")
    @Expose
    var state: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: Data? = null

    inner class Data {
        @SerializedName("content")
        @Expose
        var content: List<Content>? = null

        @SerializedName("pageable")
        @Expose
        var pageable: Pageable? = null

        @SerializedName("last")
        @Expose
        var last: Boolean? = null

        @SerializedName("totalElements")
        @Expose
        var totalElements: Int? = null

        @SerializedName("totalPages")
        @Expose
        var totalPages: Int? = null

        @SerializedName("number")
        @Expose
        var number: Int? = null

        @SerializedName("first")
        @Expose
        var first: Boolean? = null

        @SerializedName("sort")
        @Expose
        var sort: List<Any>? = null

        @SerializedName("size")
        @Expose
        var size: Int? = null

        @SerializedName("numberOfElements")
        @Expose
        var numberOfElements: Int? = null

        @SerializedName("empty")
        @Expose
        var empty: Boolean? = null

        inner class Content {
            @SerializedName("title")
            @Expose
            var title: String? = null

            @SerializedName("price")
            @Expose
            var price: Int? = null

            @SerializedName("rentalType")
            @Expose
            var rentalType: String? = null

            @SerializedName("image")
            @Expose
            var image: Image? = null

            @SerializedName("createdAt")
            @Expose
            var createdAt: String? = null

            inner class Image {
                @SerializedName("idx")
                @Expose
                var idx: String? = null

                @SerializedName("url")
                @Expose
                var url: String? = null
            }
        }

        inner class Pageable {
            @SerializedName("pageNumber")
            @Expose
            var pageNumber: Int? = null

            @SerializedName("pageSize")
            @Expose
            var pageSize: Int? = null

            @SerializedName("sort")
            @Expose
            var sort: List<Any>? = null

            @SerializedName("offset")
            @Expose
            var offset: Int? = null

            @SerializedName("paged")
            @Expose
            var paged: Boolean? = null

            @SerializedName("unpaged")
            @Expose
            var unpaged: Boolean? = null
        }
    }
}