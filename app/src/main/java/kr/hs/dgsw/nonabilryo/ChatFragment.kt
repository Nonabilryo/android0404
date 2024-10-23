package kr.hs.dgsw.nonabilryo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatListAdapter: ChatListAdapter
    private val chatItems = mutableListOf<ChatItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        // 리사이클러뷰 설정
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 샘플 데이터 추가
        loadDummyChatItems()

        // 어댑터 설정
        chatListAdapter = ChatListAdapter(chatItems) { chatItem ->
            onChatItemClick(chatItem)
        }
        recyclerView.adapter = chatListAdapter

        return view
    }

    private fun loadDummyChatItems() {
        chatItems.add(ChatItem(R.drawable.ic_profile, "User1", "안녕하세요", "1시간전"))
        chatItems.add(ChatItem(R.drawable.ic_profile, "User2", "구매하고싶습니다", "12시간전"))
    }

    private fun onChatItemClick(chatItem: ChatItem) {
        // MessageActivity로 이동
        val intent = Intent(requireActivity(), MessageActivity::class.java)
        intent.putExtra("userName", chatItem.username)
        startActivity(intent)
    }
}
