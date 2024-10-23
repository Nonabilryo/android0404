package kr.hs.dgsw.nonabilryo

data class ChatMessage(
    val message: String,  // 메시지
    val isSent: Boolean   // true 보낸거 false 받은거
)
