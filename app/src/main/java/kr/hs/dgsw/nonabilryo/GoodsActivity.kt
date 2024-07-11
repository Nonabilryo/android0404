package kr.hs.dgsw.nonabilryo

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoodsActivity : AppCompatActivity() {
    private lateinit var goodsImageView: ImageView
    private lateinit var writerTextView: TextView
    private lateinit var goodsTitleTextView: TextView
    private lateinit var priceTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var categoryTextView: TextView
    private lateinit var timeTextView: TextView

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

        val backButton: ImageButton = findViewById(R.id.back_btn)
        backButton.setOnClickListener {
            finish()
        }

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation_view)

        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    true
                }
                R.id.navigation_community -> {
                    true
                }
                R.id.navigation_chat -> {
                    true
                }
                R.id.navigation_my -> {
                    true
                }
                else -> false
            }
        }

        bottomNavigationView.selectedItemId = R.id.navigation_home

        // Retrofit 호출
        fetchArticleDetail("1b585769-3f01-4cb6-ba54-d0c30b95cb1b")
    }

    private fun fetchArticleDetail(articleId: String) {
        val call = RetrofitClient.instance.getArticleById(articleId)
        call.enqueue(object : Callback<ArticleDetailResponse> {
            override fun onResponse(call: Call<ArticleDetailResponse>, response: Response<ArticleDetailResponse>) {
                if (response.isSuccessful) {
                    val articleDetail = response.body()
                    if (articleDetail != null) {
                        Log.d("API 호출 성공", "응답 내용: $articleDetail")
                        // Update UI with the data
                        updateUI(articleDetail.data)
                    } else {
                        Log.d("API 호출 성공", "응답이 비어 있습니다.")
                    }
                } else {
                    Log.d("API 호출 실패", "${response.code()} - ${response.message()}")
                    response.errorBody()?.let {
                        Log.d("API 호출 실패", "오류 본문: ${it.string()}")
                    }
                }
            }

            override fun onFailure(call: Call<ArticleDetailResponse>, t: Throwable) {
                Log.d("API 호출 오류", t.message ?: "알 수 없는 오류")
            }
        })
    }

    private fun updateUI(articleData: ArticleData) {
        goodsTitleTextView.text = articleData.title
        writerTextView.text = articleData.writer
        priceTextView.text = "${articleData.price}원"
        descriptionTextView.text = articleData.description
        categoryTextView.text = articleData.category
        timeTextView.text = articleData.createdAt

        // Load image using Glide
        if (articleData.images.isNotEmpty()) {
            Glide.with(this)
                .load(articleData.images[0].url)
                .into(goodsImageView)
        }
    }
}