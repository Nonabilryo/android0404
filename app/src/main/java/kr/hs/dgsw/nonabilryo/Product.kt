package kr.hs.dgsw.nonabilryo

data class Product(
    val idx: String,
    val title: String,
    val price: Int,
    val rentalType: String,
    val image: Image
)

data class Image(
    val url: String,
    val idx: String
)