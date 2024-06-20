package kr.hs.dgsw.nonabilryo

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
    private lateinit var sharedPreferencesManager: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        nameEdit = findViewById(R.id.name_edit)
        idEdit = findViewById(R.id.id_edit)
        pwEdit = findViewById(R.id.pw_edit)
        emailEdit = findViewById(R.id.email_edit)
        tellEdit = findViewById(R.id.tell_edit)
        addressEdit = findViewById(R.id.adress_edit)
        emailVerificationEdit = findViewById(R.id.email_verification_edit)
        tellVerificationEdit = findViewById(R.id.tell_verification_edit)
        sharedPreferencesManager = SharedPreferences(this)

        val backBtn: ImageButton = findViewById(R.id.back_btn)
        backBtn.setOnClickListener {
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
        val address = if (addressEdit.text.toString().isNotEmpty()) addressEdit.text.toString() else null
        val emailVerifyCode = emailVerificationEdit.text.toString()
        val tellVerifyCode = tellVerificationEdit.text.toString()

        if (emailVerifyCode.isEmpty()) {
            showToast("이메일 인증 코드를 입력해주세요.")
            return
        }

        if (tellVerifyCode.isEmpty()) {
            showToast("전화번호 인증 코드를 입력해주세요.")
            return
        }

        val signupRequest = SignupRequest(name, id, password, address, email, tell, emailVerifyCode, tellVerifyCode)

        Log.d("SignupRequest", "Request Data: $signupRequest")

        val retrofitService = RetrofitClient.instance
        val call = retrofitService.signup(signupRequest)

        call.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    val signupResponse = response.body()
                    if (signupResponse != null && signupResponse.state == 200) {
                        showToast("회원가입 성공")
                        navigateToHome()
                    } else {
                        showToast(signupResponse?.message ?: "회원가입에 실패했습니다.")
                    }
                } else {
                    showToast("서버와의 통신에 실패했습니다. 상태 코드: ${response.code()}")
                    Log.e("SignupError", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                showToast("네트워크 오류: ${t.message}")
            }
        })
    }

    fun onEmailVerifyButtonClick(view: View) {
        val email = emailEdit.text.toString()
        val retrofitService = RetrofitClient.instance
        val call = retrofitService.sendEmailVerificationCode(EmailRequest(email))

        call.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    val verificationResponse = response.body()
                    if (verificationResponse != null && verificationResponse.state == 200) {
                        showToast("이메일 인증번호 전송 성공")
                    } else {
                        val errorMessage = verificationResponse?.message ?: "이메일 인증번호 전송에 실패했습니다."
                        showToast(errorMessage)
                    }
                } else {
                    showToast("서버와의 통신에 실패했습니다. 상태 코드: ${response.code()}")
                    Log.e("SignupError", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                showToast("네트워크 오류: ${t.message}")
            }
        })
    }

    fun onTellVerifyButtonClick(view: View) {
        val tell = tellEdit.text.toString()
        val retrofitService = RetrofitClient.instance
        val call = retrofitService.sendPhoneVerificationCode(PhoneRequest(tell))

        call.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    val verificationResponse = response.body()
                    if (verificationResponse != null && verificationResponse.state == 200) {
                        showToast("전화번호 인증번호 전송 성공")
                    } else {
                        val errorMessage = verificationResponse?.message ?: "전화번호 인증번호 전송에 실패했습니다."
                        showToast(errorMessage)
                    }
                } else {
                    showToast("서버와의 통신에 실패했습니다. 상태 코드: ${response.code()}")
                    Log.e("SignupError", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                showToast("네트워크 오류: ${t.message}")
            }
        })
    }

    fun onNameVerifyButtonClick(view: View) {
        val name = nameEdit.text.toString()
        val retrofitService = RetrofitClient.instance
        val call = retrofitService.verifyName(NameRequest(name))

        call.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    val nameVerifyResponse = response.body()
                    if (nameVerifyResponse != null && nameVerifyResponse.state == 200) {
                        showToast("이름 중복 확인 성공")
                    } else {
                        val errorMessage = nameVerifyResponse?.message ?: "이름 중복 확인에 실패했습니다."
                        showToast(errorMessage)
                    }
                } else {
                    showToast("서버와의 통신에 실패했습니다. 상태 코드: ${response.code()}")
                    Log.e("SignupError", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                showToast("네트워크 오류: ${t.message}")
            }
        })
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeFragment::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}