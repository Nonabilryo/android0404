package kr.hs.dgsw.nonabilryo

data class ArticleDetailResponse(
    val state: Int,
    val message: String,
    val data: ArticleData
)

data class ArticleData(
    val title: String,
    val images: List<ArticleImage>,
    val writer: String,
    val category: String,
    val description: String,
    val price: Int,
    val rentalType: String,
    val createdAt: String
)

data class ArticleImage(
    val idx: String,
    val url: String
)
