package kr.hs.dgsw.nonabilryo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var idEdit: EditText
    private lateinit var pwEdit: EditText
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        idEdit = findViewById(R.id.id_edit)
        pwEdit = findViewById(R.id.pw_edit)
        sharedPreferencesManager = SharedPreferencesManager(this)

        val loginBtn: AppCompatButton = findViewById(R.id.login_btn)
        loginBtn.setOnClickListener {
            val userId = idEdit.text.toString()
            val password = pwEdit.text.toString()
            if (userId.isNotEmpty() && password.isNotEmpty()) {
                val loginRequest = LoginRequest(userId, password)
                login(loginRequest)
            } else {
                showToast("아이디와 비밀번호를 입력해주세요")
            }
        }

        val findSignup: AppCompatButton = findViewById(R.id.find_signup)
        findSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        val backButton: ImageButton = findViewById(R.id.back_btn)
        backButton.setOnClickListener { finish() }
    }

    // 뒤로가기 버튼 클릭 시 호출될 메서드
    fun onBackButtonClick(view: View) {
        onBackPressed()
    }

    private fun login(loginRequest: LoginRequest) {
        RetrofitClient.instance.login(loginRequest)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse != null && loginResponse.state == 200) {
                            showToast("로그인 성공")
                            sharedPreferencesManager.saveTokens(loginResponse.data.accessToken, loginResponse.data.refreshToken)
                            Log.d("bbb", "" + sharedPreferencesManager.getAccessToken())
                            navigateToHome()
                        } else {
                            showToast(loginResponse?.message ?: "로그인 실패")
                        }
                    } else {
                        showToast("서버 오류")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    showToast("네트워크 오류: ${t.message}")
                }
            })
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}