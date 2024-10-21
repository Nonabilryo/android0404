package kr.hs.dgsw.nonabilryo

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
    private lateinit var emailEdit: EditText
    private lateinit var tellEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var emailVerificationEdit: EditText
    private lateinit var tellVerificationEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        nameEdit = findViewById(R.id.name_edit)
        emailEdit = findViewById(R.id.email_edit)
        tellEdit = findViewById(R.id.tell_edit)
        passwordEdit = findViewById(R.id.pw_edit)
        emailVerificationEdit = findViewById(R.id.email_verification_edit)
        tellVerificationEdit = findViewById(R.id.tell_verification_edit)

        val backButton: ImageButton = findViewById(R.id.back_btn)
        backButton.setOnClickListener { finish() }
    }

    fun onNameVerifyButtonClick(view: View) {
        val name = nameEdit.text.toString()
        val retrofitService = RetrofitClient.instance
        val call = retrofitService.verifyName(NameVerifyRequest(name))

        call.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    val verificationResponse = response.body()
                    if (verificationResponse != null && verificationResponse.state == 200) {
                        showToast("이름 중복 확인 성공")
                    } else {
                        val errorMessage = verificationResponse?.message ?: "이름 중복 확인 실패."
                        showToast(errorMessage)
                    }
                } else {
                    showToast("서버와의 통신에 실패했습니다. 상태 코드: ${response.code()}")
                    Log.e("NameVerifyError", "Error: ${response.errorBody()?.string()}")
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
        val call = retrofitService.verifyEmail(EmailRequest(email))

        call.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    val verificationResponse = response.body()
                    if (verificationResponse != null && verificationResponse.state == 200) {
                        showToast("이메일 확인 성공")
                    } else {
                        val errorMessage = verificationResponse?.message ?: "이메일 확인 실패."
                        showToast(errorMessage)
                    }
                } else {
                    showToast("서버와의 통신에 실패했습니다. 상태 코드: ${response.code()}")
                    Log.e("EmailVerifyError", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                showToast("네트워크 오류: ${t.message}")
            }
        })
    }

    fun onEmailVerifyCodeButtonClick(view: View) {
        val emailVerifyCode = emailVerificationEdit.text.toString()
        val retrofitService = RetrofitClient.instance
        val call = retrofitService.verifyEmailCode(EmailVerifyCodeRequest(emailVerifyCode))

        call.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    val verificationResponse = response.body()
                    if (verificationResponse != null && verificationResponse.state == 200) {
                        showToast("이메일 인증 성공")
                    } else {
                        val errorMessage = verificationResponse?.message ?: "이메일 인증 실패."
                        showToast(errorMessage)
                    }
                } else {
                    showToast("서버와의 통신에 실패했습니다. 상태 코드: ${response.code()}")
                    Log.e("EmailCodeVerifyError", "Error: ${response.errorBody()?.string()}")
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
        val call = retrofitService.verifyTell(TellRequest(tell))

        call.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    val verificationResponse = response.body()
                    if (verificationResponse != null && verificationResponse.state == 200) {
                        showToast("전화번호 확인 성공")
                    } else {
                        val errorMessage = verificationResponse?.message ?: "전화번호 확인 실패."
                        showToast(errorMessage)
                    }
                } else {
                    showToast("서버와의 통신에 실패했습니다. 상태 코드: ${response.code()}")
                    Log.e("TellVerifyError", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                showToast("네트워크 오류: ${t.message}")
            }
        })
    }

    fun onTellVerifyCodeButtonClick(view: View) {
        val tellVerifyCode = tellVerificationEdit.text.toString()
        val retrofitService = RetrofitClient.instance
        val call = retrofitService.verifyTellCode(TellVerifyCodeRequest(tellVerifyCode))

        call.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    val verificationResponse = response.body()
                    if (verificationResponse != null && verificationResponse.state == 200) {
                        showToast("전화번호 인증 성공")
                    } else {
                        val errorMessage = verificationResponse?.message ?: "전화번호 인증 실패."
                        showToast(errorMessage)
                    }
                } else {
                    showToast("서버와의 통신에 실패했습니다. 상태 코드: ${response.code()}")
                    Log.e("TellCodeVerifyError", "Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                showToast("네트워크 오류: ${t.message}")
            }
        })
    }

    fun onSignupButtonClick(view: View) {
        val name = nameEdit.text.toString()
        val id = "dAhFJ283Ich" // 여기에 실제 ID를 입력해야 합니다.
        val password = passwordEdit.text.toString()
        val email = emailEdit.text.toString()
        val tell = tellEdit.text.toString()
        val address = "- - - -" // 여기에 실제 주소를 입력해야 합니다.
        val emailVerifyCode = emailVerificationEdit.text.toString()
        val tellVerifyCode = tellVerificationEdit.text.toString()

        val signUpRequest = SignUpRequest(
            name,
            id,
            password,
            email,
            tell,
            address,
            emailVerifyCode,
            tellVerifyCode
        )

        val retrofitService = RetrofitClient.instance
        val call = retrofitService.signUp(signUpRequest)

        call.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    val signUpResponse = response.body()
                    if (signUpResponse != null && signUpResponse.state == 200) {
                        showToast("회원가입 성공")
                        // 회원가입 성공 후 처리
                    } else {
                        val errorMessage = signUpResponse?.message ?: "회원가입 실패."
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}