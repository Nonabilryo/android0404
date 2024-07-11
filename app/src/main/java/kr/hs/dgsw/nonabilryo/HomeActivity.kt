package kr.hs.dgsw.nonabilryo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class HomeActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation_view)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    // Handle Home navigation
                    true
                }
                R.id.navigation_community -> {
                    // Handle Community navigation
                    true
                }
                R.id.navigation_chat -> {
                    // Handle Chat navigation
                    true
                }
                R.id.navigation_my -> {
                    // Handle My navigation
                    true
                }
                else -> false
            }
        }

        // Set default fragment or action
        bottomNavigationView.selectedItemId = R.id.navigation_home

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ArticleAdapter(listOf()) { article ->
            val intent = Intent(this, GoodsActivity::class.java)
            intent.putExtra("ARTICLE_TITLE", article.title)  // ARTICLE_TITLE로 제목을 전달
            intent.putExtra("ARTICLE_PRICE", article.price)  // ARTICLE_PRICE로 가격을 전달
            intent.putExtra("ARTICLE_RENTAL_TYPE", article.rentalType)  // ARTICLE_RENTAL_TYPE로 대여 유형을 전달
            intent.putExtra("ARTICLE_IMAGE_URL", article.image.url)  // ARTICLE_IMAGE_URL로 이미지 URL을 전달
            intent.putExtra("ARTICLE_CREATED_AT", article.createdAt)  // ARTICLE_CREATED_AT로 생성 날짜를 전달
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        // Retrofit 호출
        checkApiResponse()
    }

    private fun checkApiResponse() {
        val call = RetrofitClient.instance.getArticle("0")
        call.enqueue(object : Callback<ArticleResponse> {
            override fun onResponse(call: Call<ArticleResponse>, response: Response<ArticleResponse>) {
                if (response.isSuccessful) {
                    // 200 응답 처리
                    val articleResponse = response.body()
                    if (articleResponse != null) {
                        println("API 호출 성공: $articleResponse")
                        // 어댑터에 데이터 업데이트
                        adapter.updateArticles(articleResponse.data.content)
                    } else {
                        println("응답이 비어 있습니다.")
                    }
                } else {
                    // 오류 처리
                    println("API 호출 실패: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                // 네트워크 오류 등 처리
                println("API 호출 오류: ${t.message}")
            }
        })
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