package kr.hs.dgsw.nonabilryo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {

    private lateinit var nameEdit: EditText
    private lateinit var idEdit: EditText
    private lateinit var pwEdit: EditText
    private lateinit var emailEdit: EditText
    private lateinit var tellEdit: EditText
    private lateinit var addressEdit: EditText
    private lateinit var emailVerificationEdit: EditText
    private lateinit var tellVerificationEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        nameEdit = findViewById(R.id.name_edit)
        idEdit = findViewById(R.id.id_edit)
        pwEdit = findViewById(R.id.pw_edit)
        emailEdit = findViewById(R.id.email_edit)
        tellEdit = findViewById(R.id.tell_edit)
        addressEdit = findViewById(R.id.address_edit)
        emailVerificationEdit = findViewById(R.id.email_verification_edit)
        tellVerificationEdit = findViewById(R.id.tell_verification_edit)

        val backBtn: ImageButton = findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
            // 뒤로 가기 동작
            onBackPressed()
        }
    }

    fun onBackButtonClick(view: View) {
        onBackPressed()
    }

    fun onSignupButtonClick(view: View) {
        val name = nameEdit.text.toString()
        val id = idEdit.text.toString()
        val password = pwEdit.text.toString()
        val email = emailEdit.text.toString()
        val tell = tellEdit.text.toString()
        val address = addressEdit.text.toString()
        val signupRequest = SignupRequest(name, id, password, email, tell, address)
        val retrofitService = RetrofitClient.instance
        val call = retrofitService.signup(signupRequest)

        call.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    val signupResponse = response.body()
                    if (signupResponse != null && signupResponse.state == 200) {
                        navigateToHome()
                    } else {
                        showToast(signupResponse?.message ?: "회원가입에 실패했습니다.")
                    }
                } else {
                    showToast("서버와의 통신에 실패했습니다.")
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                showToast("네트워크 오류: ${t.message}")
            }
        })
    }

    fun onEmailVerifyButtonClick(view: View) {
        val email = emailEdit.text.toString()
        val emailVerifyCode = emailVerificationEdit.text.toString()
        val retrofitService = RetrofitClient.instance
        val call = retrofitService.verifyEmail(EmailVerificationRequest(email, emailVerifyCode))

        call.enqueue(object : Callback<VerificationResponse> {
            override fun onResponse(call: Call<VerificationResponse>, response: Response<VerificationResponse>) {
                if (response.isSuccessful) {
                    val verificationResponse = response.body()
                    if (verificationResponse != null && verificationResponse.success) {
                        showToast("이메일 인증 성공")
                    } else {
                        val errorMessage = verificationResponse?.errorMessage ?: "이메일 인증에 실패했습니다."
                        showToast(errorMessage)
                    }
                } else {
                    showToast("서버와의 통신에 실패했습니다.")
                }
            }

            override fun onFailure(call: Call<VerificationResponse>, t: Throwable) {
                showToast("네트워크 오류: ${t.message}")
            }
        })
    }

    fun onTellVerifyButtonClick(view: View) {
        val phone = tellEdit.text.toString()
        val tellVerifyCode = tellVerificationEdit.text.toString()
        val retrofitService = RetrofitClient.instance
        val call = retrofitService.verifyPhone(PhoneVerificationRequest(phone, tellVerifyCode))

        call.enqueue(object : Callback<VerificationResponse> {
            override fun onResponse(call: Call<VerificationResponse>, response: Response<VerificationResponse>) {
                if (response.isSuccessful) {
                    val verificationResponse = response.body()
                    if (verificationResponse != null && verificationResponse.success) {
                        showToast("전화번호 인증 성공")
                    } else {
                        val errorMessage = verificationResponse?.errorMessage ?: "전화번호 인증에 실패했습니다."
                        showToast(errorMessage)
                    }
                } else {
                    showToast("서버와의 통신에 실패했습니다.")
                }
            }

            override fun onFailure(call: Call<VerificationResponse>, t: Throwable) {
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