package kr.hs.dgsw.nonabilryo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val messages: List<ChatMessage>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    // ViewHolder 클래스: 각 메시지를 표시하는 뷰를 보유
    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
    }

    // onCreateViewHolder: 뷰 홀더를 생성, 보낸 메시지와 받은 메시지에 따라 레이아웃을 선택
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layoutId = if (viewType == VIEW_TYPE_SENT) R.layout.item_sent else R.layout.item_received
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ChatViewHolder(view)
    }

    // onBindViewHolder: 메시지 데이터 바인딩
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        holder.messageTextView.text = message.message
    }

    // getItemViewType: 메시지 타입에 따라 뷰 타입을 반환 (보낸 메시지, 받은 메시지)
    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isSent) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    // getItemCount: 메시지의 총 개수 반환
    override fun getItemCount(): Int = messages.size

    // 상수 정의: 보낸 메시지와 받은 메시지 구분
    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }
}
