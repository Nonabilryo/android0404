package kr.hs.dgsw.nonabilryo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient

class MainActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private lateinit var googleApiClient: GoogleApiClient
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginButton: Button = findViewById(R.id.login_btn)
        val signupButton: Button = findViewById(R.id.signup_textbtn)
        val googleButton: Button = findViewById(R.id.google_btn)

        // 로그인 버튼 클릭 시 로그인 페이지로 이동
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // 회원가입 버튼 클릭 시 회원가입 페이지로 이동
        signupButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        // 구글 로그인 버튼 클릭 시
        googleButton.setOnClickListener {
            signInWithGoogle()
        }

        // 구글 로그인 설정
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this, this)
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
    }

    // 구글 로그인 시도
    private fun signInWithGoogle() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // 구글 로그인 결과 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val result = data?.let { Auth.GoogleSignInApi.getSignInResultFromIntent(it) }
            if (result != null && result.isSuccess) {
                val account = result.signInAccount
                // TODO: 구글 계정으로 로그인 처리
                Toast.makeText(this, "구글 로그인 성공", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "구글 로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 구글 로그인 실패 처리
    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Toast.makeText(this, "구글 로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
    }
}