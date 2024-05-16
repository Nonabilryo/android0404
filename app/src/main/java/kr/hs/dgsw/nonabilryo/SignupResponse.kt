package kr.hs.dgsw.nonabilryo

data class SignupResponse(
    val success: Boolean,
    val errorMessage: String? = null
)