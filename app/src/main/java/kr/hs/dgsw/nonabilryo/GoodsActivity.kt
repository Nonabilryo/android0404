package kr.hs.dgsw.nonabilryo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoodsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods)

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
                        // articleDetail.data를 이용해 UI를 업데이트합니다.
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
}