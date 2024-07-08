package kr.hs.dgsw.nonabilryo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

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

        // RecyclerView 설정
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val itemList = listOf(
            Item("Title 1", "테스트~", "1000원", R.drawable.logo_img, 10),
            Item("Title 2", "설명", "1000원", R.drawable.logo_img, 20)
        )

        val adapter = RecyclerAdapter(itemList)
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
}