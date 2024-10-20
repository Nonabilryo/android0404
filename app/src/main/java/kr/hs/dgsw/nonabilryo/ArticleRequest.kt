package kr.hs.dgsw.nonabilryo

data class ArticleRequest(
    val title: String,
    val category: String,
    val description: String,
    val price: Int,
    val rentalType: Int,
    val images: List<String>
)