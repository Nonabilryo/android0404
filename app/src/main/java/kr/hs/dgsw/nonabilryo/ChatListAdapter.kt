package kr.hs.dgsw.nonabilryo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatListAdapter(
    private val chatList: List<ChatItem>,
    private val itemClickListener: (ChatItem) -> Unit
) : RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.profileImage)
        val username: TextView = itemView.findViewById(R.id.tv_name)
        val lastMessage: TextView = itemView.findViewById(R.id.tv_message)
        val timestamp: TextView = itemView.findViewById(R.id.tv_timestamp)

        init {
            // 클릭 리스너 설정
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener(chatList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatItem = chatList[position]
        holder.profileImage.setImageResource(chatItem.profileImage)
        holder.username.text = chatItem.username
        holder.lastMessage.text = chatItem.lastMessage
        holder.timestamp.text = chatItem.timestamp
    }

    override fun getItemCount() = chatList.size
}