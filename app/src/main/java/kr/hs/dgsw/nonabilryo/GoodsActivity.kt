package kr.hs.dgsw.nonabilryo

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class GoodsActivity : AppCompatActivity() {
    private lateinit var goodsImageView: ImageView
    private lateinit var writerTextView: TextView
    private lateinit var goodsTitleTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var categoryTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var rentalTypeTextView: TextView
    private lateinit var heartButton: ImageButton
    private var isHearted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods)

        goodsImageView = findViewById(R.id.goods_img)
        writerTextView = findViewById(R.id.writer)
        goodsTitleTextView = findViewById(R.id.goods_title)
        priceTextView = findViewById(R.id.price)
        descriptionTextView = findViewById(R.id.description)
        categoryTextView = findViewById(R.id.category)
        timeTextView = findViewById(R.id.time)
        rentalTypeTextView = findViewById(R.id.rental_type)
        heartButton = findViewById(R.id.heart_btn)

        val backButton: ImageButton = findViewById(R.id.back_btn)
        backButton.setOnClickListener { finish() }

        // articleIdx로 API 호출
        val articleIdx = "df1f8741-4b26-424c-8f19-f15314c96b7e"
        Log.d("GoodsActivity", "articleIdx: $articleIdx")
        fetchArticleDetail(articleIdx)

        // 하트 버튼 클릭 리스너 설정
        heartButton.setOnClickListener {
            toggleHeart()
        }
    }

    private fun fetchArticleDetail(articleIdx: String) {
        Log.d("GoodsActivity", "fetchArticleDetail 호출됨: articleIdx = $articleIdx")
        val call = RetrofitClient.instance.getArticleById(articleIdx)
        call.enqueue(object : Callback<ArticleDetailResponse> {
            override fun onResponse(call: Call<ArticleDetailResponse>, response: Response<ArticleDetailResponse>) {
                if (response.isSuccessful) {
                    val articleDetail = response.body()
                    Log.d("GoodsActivity", "API 호출 성공: 응답 코드 = ${response.code()}")

                    if (articleDetail != null && articleDetail.data != null) {
                        Log.d("GoodsActivity", "API 데이터 수신 성공: ${articleDetail.data}")
                        updateUI(articleDetail.data)
                    } else {
                        Log.d("GoodsActivity", "API 응답은 성공했으나 데이터가 비어 있음")
                    }
                } else {
                    Log.d("GoodsActivity", "API 호출 실패: 응답 코드 = ${response.code()}, 메시지 = ${response.message()}")
                    response.errorBody()?.let {
                        Log.d("GoodsActivity", "API 호출 실패: 오류 본문 = ${it.string()}")
                    }
                }
            }

            override fun onFailure(call: Call<ArticleDetailResponse>, t: Throwable) {
                Log.d("GoodsActivity", "API 호출 오류: ${t.message}")
            }
        })
    }

    private fun updateUI(articleData: ArticleData) {
        Log.d("GoodsActivity", "UI 업데이트 시작")
        goodsTitleTextView.text = articleData.title

        val writerName = extractNameFromWriter(articleData.writer)
        writerTextView.text = writerName

        priceTextView.text = "${articleData.price}원"
        descriptionTextView.text = articleData.description
        categoryTextView.text = articleData.category
        timeTextView.text = getRelativeTime(articleData.createdAt)

        rentalTypeTextView.text = articleData.rentalType

        if (articleData.images.isNotEmpty()) {
            Log.d("GoodsActivity", "이미지 로드 중: ${articleData.images[0].url}")
            Glide.with(this)
                .load(articleData.images[0].url)
                .into(goodsImageView)
        } else {
            Log.d("GoodsActivity", "이미지가 없습니다.")
        }
        Log.d("GoodsActivity", "UI 업데이트 완료")
    }

    private fun extractNameFromWriter(writer: String): String {
        val regex = Regex("name=(.*?),")
        val matchResult = regex.find(writer)
        return matchResult?.groups?.get(1)?.value ?: "작성자 없음"
    }

    private fun getRelativeTime(createdAt: String): String {
        Log.d("GoodsActivity", "getRelativeTime 호출됨: createdAt = $createdAt")
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        return try {
            val createdAtDate = formatter.parse(createdAt)
            val now = Date()
            val duration = now.time - createdAtDate.time

            val days = TimeUnit.MILLISECONDS.toDays(duration)
            val hours = TimeUnit.MILLISECONDS.toHours(duration)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)

            when {
                days > 365 -> "${days / 365}년 전"
                days > 30 -> "${days / 30}개월 전"
                days > 0 -> "${days}일 전"
                hours > 0 -> "${hours}시간 전"
                minutes > 0 -> "${minutes}분 전"
                else -> "방금 전"
            }
        } catch (e: ParseException) {
            Log.e("GoodsActivity", "날짜 형식 오류: ${e.message}")
            "날짜 형식 오류"
        }
    }

    private fun toggleHeart() {
        isHearted = !isHearted // 하트 상태 토글

        if (isHearted) {
            heartButton.setImageResource(R.drawable.ic_red_heart) // 빨간 하트 이미지
            heartButton.setColorFilter(getColor(R.color.red)) // 빨간색 필터
        } else {
            heartButton.setImageResource(R.drawable.ic_heart) // 원래 하트 이미지
            heartButton.clearColorFilter() // 필터 제거
        }
    }
}