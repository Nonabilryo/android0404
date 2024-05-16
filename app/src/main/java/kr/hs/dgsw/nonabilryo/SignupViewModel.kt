package kr.hs.dgsw.nonabilryo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kr.hs.dgsw.nonabilryo.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel : ViewModel() {
    private val _signupCompleted = MutableLiveData<Boolean>()
    val signupCompleted: LiveData<Boolean>
        get() = _signupCompleted

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun onSignupButtonClicked(
        name: String,
        id: String,
        password: String,
        rePassword: String,
        email: String,
        tell: String
    ) {
        // 비밀번호와 재확인 비밀번호가 일치하는지 확인
        if (password != rePassword) {
            _errorMessage.value = "비밀번호가 일치하지 않습니다."
            return
        }

        // Retrofit을 사용하여 서버로 회원가입 요청 보내기
        val retrofitService = RetrofitClient.instance
        val call = retrofitService.signup(name, id, password, email, tell)

        call.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    val signupResponse = response.body()
                    if (signupResponse != null && signupResponse.success) {
                        // 회원가입 성공
                        _signupCompleted.value = true
                    } else {
                        // 회원가입 실패
                        _errorMessage.value = signupResponse?.errorMessage ?: "회원가입에 실패했습니다."
                    }
                } else {
                    // 서버와 통신 실패
                    _errorMessage.value = "서버와의 통신에 실패했습니다."
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                // 네트워크 오류
                _errorMessage.value = "네트워크 오류: ${t.message}"
            }
        })
    }
}