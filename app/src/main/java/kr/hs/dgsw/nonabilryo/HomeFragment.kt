package kr.hs.dgsw.nonabilryo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ArticleAdapter
    private var articles: MutableList<ArticleResponse.Data.Article> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val plusButton: FloatingActionButton = view.findViewById(R.id.plus_btn)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = ArticleAdapter(articles) { article ->
            val intent = Intent(activity, GoodsActivity::class.java)
            intent.putExtra("ARTICLE_TITLE", article.title)
            intent.putExtra("ARTICLE_PRICE", article.price)
            intent.putExtra("ARTICLE_RENTAL_TYPE", article.rentalType)
            intent.putExtra("ARTICLE_IMAGE_URL", article.image.url)
            intent.putExtra("ARTICLE_CREATED_AT", article.createdAt)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        // Retrofit 호출
        checkApiResponse()

        plusButton.setOnClickListener {
            startActivity(Intent(activity, GoodsAddActivity::class.java)) // 상품 등록 페이지로 이동
        }

        return view
    }

    private fun checkApiResponse() {
        val call = RetrofitClient.instance.getArticle("0")
        call.enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(call: Call<ArticleResponse>, response: Response<ArticleResponse>) {
                if (response.isSuccessful) {
                    val articleResponse = response.body()
                    if (articleResponse != null) {
                        updateArticles(articleResponse.data.content)
                    }
                } else {
                    println("API 호출 실패: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                println("API 호출 오류: ${t.message}")
            }
        })
    }

    private fun updateArticles(newArticles: List<ArticleResponse.Data.Article>) {
        articles.clear()
        articles.addAll(newArticles)
        adapter.notifyDataSetChanged()
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