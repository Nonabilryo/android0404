package kr.hs.dgsw.nonabilryo

data class Item(
    val titleText: String,
    val infoText: String,
    val priceText: String,
    val imageId: Int,
    var heartCount: Int
)
