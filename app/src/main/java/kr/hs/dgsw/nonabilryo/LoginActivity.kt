package kr.hs.dgsw.nonabilryo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import kr.hs.dgsw.nonabilryo.HomeActivity
import kr.hs.dgsw.nonabilryo.SignupActivity
import kr.hs.dgsw.nonabilryo.RetrofitClient
import kr.hs.dgsw.nonabilryo.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var idEdit: EditText
    private lateinit var pwEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        idEdit = findViewById(R.id.id_edit)
        pwEdit = findViewById(R.id.pw_edit)

        val loginBtn: AppCompatButton = findViewById(R.id.login_btn)
        loginBtn.setOnClickListener {
            val userId = idEdit.text.toString()
            val password = pwEdit.text.toString()
            if (userId.isNotEmpty() && password.isNotEmpty()) {
                login(userId, password)
            } else {
                showToast("아이디와 비밀번호를 입력해주세요")
            }
        }

        val findSignup: AppCompatButton = findViewById(R.id.find_signup)
        findSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        val backBtn: ImageButton = findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
            // 뒤로 가기 동작
            onBackPressed()
        }
    }

    fun onBackButtonClick(view: View) {
        onBackPressed()
    }

    private fun login(userId: String, password: String) {
        RetrofitClient.instance.login(userId, password)
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResult = response.body()
                        if (loginResult != null && loginResult.success) {
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            finish()
                        } else {
                            showToast(loginResult?.message ?: "로그인 실패")
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}