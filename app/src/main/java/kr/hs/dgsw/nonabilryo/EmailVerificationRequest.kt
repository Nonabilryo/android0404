package kr.hs.dgsw.nonabilryo

data class EmailVerificationRequest(val email: String, val code: String)
