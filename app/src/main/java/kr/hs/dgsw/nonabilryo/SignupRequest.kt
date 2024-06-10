package kr.hs.dgsw.nonabilryo

data class SignupRequest(
    val name: String,
    val id: String,
    val password: String,
    val address: String,
    val email: String,
    val phone: String
)