package kr.hs.dgsw.nonabilryo

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MessageActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 어댑터 초기화
        chatAdapter = ChatAdapter(messages)
        recyclerView.adapter = chatAdapter

        val decoration = RecyclerViewDecoration(40)
        recyclerView.addItemDecoration(decoration)

        loadDummyMessages()

        val backButton: ImageButton = findViewById(R.id.back_btn)
        backButton.setOnClickListener { finish() }
    }


    private fun loadDummyMessages() {
        // 채팅 메시지 추가
        messages.add(ChatMessage("안녕! 이건 보낸 메시지야", true))
        messages.add(ChatMessage("안녕! 이건 보낸 메시지야", true))
        messages.add(ChatMessage("안녕하세요! 이건 받은 메시지예요", false))
        messages.add(ChatMessage("오, 반가워!", true))
        messages.add(ChatMessage("네, 저도 반가워요!", false))

        // 어댑터에 데이터 변경
        chatAdapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(messages.size - 1)
    }
}