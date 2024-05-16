package kr.hs.dgsw.nonabilryo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kr.hs.dgsw.nonabilryo.HomeActivity
import kr.hs.dgsw.nonabilryo.RetrofitClient
import kr.hs.dgsw.nonabilryo.SignupResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    private lateinit var nameEdit: EditText
    private lateinit var idEdit: EditText
    private lateinit var pwEdit: EditText
    private lateinit var rePwEdit: EditText
    private lateinit var emailEdit: EditText
    private lateinit var tellEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        nameEdit = findViewById(R.id.name_edit)
        idEdit = findViewById(R.id.id_edit)
        pwEdit = findViewById(R.id.pw_edit)
        rePwEdit = findViewById(R.id.re_pw_edit)
        emailEdit = findViewById(R.id.email_edit)
        tellEdit = findViewById(R.id.tell_edit)

        val backBtn: ImageButton = findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
            // 뒤로 가기 동작
            onBackPressed()
        }
    }

    fun onBackButtonClick(view: View) {
        onBackPressed()
    }

    fun onSignupButtonClick(view: android.view.View) {
        val name = nameEdit.text.toString()
        val id = idEdit.text.toString()
        val password = pwEdit.text.toString()
        val rePassword = rePwEdit.text.toString()
        val email = emailEdit.text.toString()
        val tell = tellEdit.text.toString()

        if (password != rePassword) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val retrofitService = RetrofitClient.instance
        val call = retrofitService.signup(name, id, password, email, tell)

        call.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    val signupResponse = response.body()
                    if (signupResponse != null && signupResponse.success) {
                        navigateToHome()
                    } else {
                        val errorMessage = signupResponse?.errorMessage ?: "회원가입에 실패했습니다."
                        Toast.makeText(this@SignupActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@SignupActivity, "서버와의 통신에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Toast.makeText(this@SignupActivity, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}