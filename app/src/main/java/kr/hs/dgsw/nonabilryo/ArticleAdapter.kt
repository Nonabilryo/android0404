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
    private var articles: List<ArticleResponse.Data.Article>,
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
        articles = newArticles
        notifyDataSetChanged()
    }

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.recycler_img)
        private val titleTextView: TextView = itemView.findViewById(R.id.recycler_title)
        private val priceTextView: TextView = itemView.findViewById(R.id.recycler_price)
        private val timeTextView: TextView = itemView.findViewById(R.id.recycler_time)

        fun bind(article: ArticleResponse.Data.Article) {
            titleTextView.text = article.title
            priceTextView.text = "${article.price}원"
            timeTextView.text = getTimeAgo(article.createdAt) // 여기서 변환된 시간을 설정

            // 예시 이미지 로드 (Glide 사용 권장)
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