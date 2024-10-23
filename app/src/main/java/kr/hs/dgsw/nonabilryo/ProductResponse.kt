package kr.hs.dgsw.nonabilryo

data class ProductResponse(
    val state: Int,
    val message: String,
    val data: ProductData
)

data class ProductData(
    val totalElements: Int,
    val totalPages: Int,
    val size: Int,
    val content: List<ProductItem>,
    val number: Int,
    val sort: Sort,
    val numberOfElements: Int,
    val pageable: Pageable,
    val first: Boolean,
    val last: Boolean,

    val empty: Boolean
)

data class ProductItem(
    val idx: String,
    val title: String,
    val price: Int,
    val rentalType: String,
    val image: ImageItem,
    val createdAt: String
)

data class ImageItem(
    val url: String,
    val idx: String
)

data class Sort(
    val empty: Boolean,
    val sorted: Boolean,
    val unsorted: Boolean
)

data class Pageable(
    val offset: Int,
    val sort: Sort,
    val paged: Boolean,
    val pageNumber: Int,
    val pageSize: Int,
    val unpaged: Boolean
)