package kr.hs.dgsw.nonabilryo

import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.net.CookieManager
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

interface RetrofitService {
    @POST("sso/sign-up") // 회원가입 API
    fun signup(
        @Body signupRequest: SignupRequest
    ): Call<SignupResponse>

    @POST("sso/login") // 로그인 API
    fun login(
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>

    @POST("sso/verify/email")
    fun sendEmailVerificationCode(@Body email: EmailRequest): Call<SignupResponse>

    @POST("sso/verify/tell")
    fun sendPhoneVerificationCode(@Body tell: PhoneRequest): Call<SignupResponse>

    @POST("sso/verify/name")
    fun verifyName(@Body nameRequest: NameRequest): Call<SignupResponse>

    @GET("article/{page}")
    fun getArticle(@Path("page") page: String): Call<ArticleResponse>

}

object RetrofitClient {
    private const val BASE_URL = "http://10.80.161.219:8080/"

    private val okHttpClient: OkHttpClient by lazy {
        val trustAllCertificates = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                //
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                //
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCertificates, java.security.SecureRandom())

        OkHttpClient.Builder()
            .cookieJar(JavaNetCookieJar(CookieManager()))
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