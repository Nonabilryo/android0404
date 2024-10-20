package kr.hs.dgsw.nonabilryo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyActivity : AppCompatActivity() {

    private lateinit var retrofitService: RetrofitService
    private lateinit var profileImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my)

        sharedPreferencesManager = SharedPreferencesManager(this)

        retrofitService = RetrofitClient.instance

        profileImageView = findViewById(R.id.profile)
        nameTextView = findViewById(R.id.tv_name)
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)


        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.navigation_community -> {
                    startActivity(Intent(this, CommunityActivity::class.java))
                    true
                }
                R.id.navigation_chat -> {
                    startActivity(Intent(this, ChatActivity::class.java))
                    true
                }
                R.id.navigation_my -> {
                    true
                }
                else -> false
            }
        }

        bottomNavigationView.selectedItemId = R.id.navigation_my


        fetchUserInfo("d950d16f-4197-4ac0-b27d-c164c46b4aa5")
        //fetchUserInfo("ac183a12-734d-4444-872f-93eaf6c11743")
    }

    private fun fetchUserInfo(userIdx: String) {
        val token: String? = sharedPreferencesManager.getAccessToken()
        retrofitService.getUserInfo(userIdx, token)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    try {
                        Log.d("UserResponse", "loginResponse: $response")
                        if (response.isSuccessful) {
                            val loginResponse = response.body()
                            Log.d("UserResponse", "loginResponse: $loginResponse")

                            // 서버로부터 온 원시 응답을 로그로 출력
                            val rawResponse = response.body()
                            val rawResponseString = response.errorBody()?.string()
                            Log.d("UserResponse", "Raw Response String: $rawResponseString")

                            rawResponse?.let { userResponse ->
                                Log.d("UserResponse", "Parsed Response Body: ${Gson().toJson(userResponse)}")
                                nameTextView.text = userResponse.data.name
                                userResponse.data.imageUrl?.let { imageUrl ->
                                    // Glide.with(this).load(imageUrl).into(profileImageView)
                                }
                            } ?: run {
                                Log.e("UserResponse", "Response body is null")
                            }
                        } else {
                            // 에러 응답을 로그로 출력
                            val errorResponse = response.errorBody()?.string()
                            Log.e("UserResponse", "Response Error: ${response.code()} - $errorResponse")
                        }
                    } catch (e: Exception) {
                        Log.e("UserResponse", "Parsing Error: ${e.localizedMessage}", e)
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.e("UserResponse", "Network Error: ${t.localizedMessage}", t)
                }
            })
    }
}