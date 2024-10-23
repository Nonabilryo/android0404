package kr.hs.dgsw.nonabilryo

data class ChatItem(
    val profileImage: Int, // 프로필 이미지 리소스 ID
    val username: String,   // 사용자 이름
    val lastMessage: String, // 마지막 메시지 내용
    val timestamp: String    // 타임스탬프
)