package kr.hs.dgsw.nonabilryo

data class ApiResponse(
    val state: Int,
    val message: String,
    val data: Any
)
