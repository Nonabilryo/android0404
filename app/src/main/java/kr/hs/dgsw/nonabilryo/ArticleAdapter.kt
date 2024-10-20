package kr.hs.dgsw.nonabilryo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class ArticleAdapter(
    private var articles: MutableList<ArticleResponse.Data.Article>,
    private val onItemClick: (ArticleResponse.Data.Article) -> Unit
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_view, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount(): Int {
        return articles.size
    }

    fun updateArticles(newArticles: List<ArticleResponse.Data.Article>) {
        val oldArticles = articles.toMutableList()
        articles.clear()
        articles.addAll(newArticles)

        // 추가된 항목 업데이트
        for (i in newArticles.indices) {
            if (i >= oldArticles.size || oldArticles[i] != newArticles[i]) {
                notifyItemInserted(i)
            }
        }

        // 삭제된 항목 업데이트
        for (i in oldArticles.indices) {
            if (i >= newArticles.size || oldArticles[i] != newArticles[i]) {
                notifyItemRemoved(i)
            }
        }
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.recycler_img)
        private val titleTextView: TextView = itemView.findViewById(R.id.recycler_title)
        private val priceTextView: TextView = itemView.findViewById(R.id.recycler_price)
        private val timeTextView: TextView = itemView.findViewById(R.id.recycler_time)

        fun bind(article: ArticleResponse.Data.Article) {
            // 제목을 15글자 미만으로 설정
            titleTextView.text = if (article.title.length > 15) {
                article.title.substring(0, 15) + "..."
            } else {
                article.title
            }
            priceTextView.text = "${article.price}원"
            timeTextView.text = getTimeAgo(article.createdAt)

            // 이미지 로드
            Glide.with(itemView).load(article.image.url).into(imageView)

            itemView.setOnClickListener {
                onItemClick(article)
            }
        }

        private fun getTimeAgo(dateString: String): String {
            val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
            val dateTime = LocalDateTime.parse(dateString, formatter)
            val now = LocalDateTime.now(ZoneOffset.UTC)

            val years = ChronoUnit.YEARS.between(dateTime, now)
            if (years > 0) return "$years" + "년 전"

            val months = ChronoUnit.MONTHS.between(dateTime, now)
            if (months > 0) return "$months" + "달 전"

            val days = ChronoUnit.DAYS.between(dateTime, now)
            if (days > 0) return "$days" + "일 전"

            val hours = ChronoUnit.HOURS.between(dateTime, now)
            if (hours > 0) return "$hours" + "시간 전"

            val minutes = ChronoUnit.MINUTES.between(dateTime, now)
            if (minutes > 0) return "$minutes" + "분 전"

            return "방금 전"
        }
    }
}
