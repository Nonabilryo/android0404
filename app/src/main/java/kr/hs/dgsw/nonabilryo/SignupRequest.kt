package kr.hs.dgsw.nonabilryo

data class SignUpRequest(
    val name: String,
    val id: String,
    val password: String,
    val email: String,
    val tell: String,
    val address: String,
    val emailVerifyCode: String,
    val tellVerifyCode: String
)

