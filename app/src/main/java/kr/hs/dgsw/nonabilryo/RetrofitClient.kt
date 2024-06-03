package kr.hs.dgsw.nonabilryo

import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

interface RetrofitService {
    @FormUrlEncoded
    @POST("sso/sign-up") // 회원가입 API 엔드포인트
    fun signup(
        @Body signupRequest: SignupRequest
    ): Call<SignupResponse>

    @POST("sso/login") // 로그인 API 엔드포인트
    fun login(
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>
}

object RetrofitClient {
    private const val BASE_URL = "http://10.80.161.250:8080/"

    private val okHttpClient: OkHttpClient by lazy {
        val trustAllCertificates = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                // No implementation needed
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                // No implementation needed
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCertificates, java.security.SecureRandom())

        OkHttpClient.Builder()
            .sslSocketFactory(sslContext.socketFactory, trustAllCertificates[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    val instance: RetrofitService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // OkHttpClient 설정 추가
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(RetrofitService::class.java)
    }
}