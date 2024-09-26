package kr.hs.dgsw.nonabilryo

import com.google.gson.GsonBuilder
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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
    @POST("sso/sign-up")
    fun signup(
        @Body signupRequest: SignupRequest
    ): Call<SignupResponse>

    @POST("sso/login")
    fun login(
        @Body loginRequest: LoginRequest
    ): Call<LoginResponse>

    @POST("sso/verify/email")
    fun sendEmailVerificationCode(@Body email: EmailRequest): Call<SignupResponse>

    @POST("sso/verify/tell")
    fun sendPhoneVerificationCode(@Body tell: PhoneRequest): Call<SignupResponse>

    @POST("sso/verify/name")
    fun verifyName(@Body nameRequest: NameRequest): Call<SignupResponse>

    @GET("article/page/{page}")
    fun getArticle(@Path("page") page: String): Call<ArticleResponse>

    @GET("article/{articleIdx}")
    fun getArticleById(@Path("articleIdx") articleIdx: String): Call<ArticleDetailResponse>

    @GET("user/{userIdx}")
    fun getUserInfo(@Path("userIdx") userIdx: String): Call<UserResponse>
}

object RetrofitClient {
    private const val BASE_URL = "http://10.80.161.246:8080/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient: OkHttpClient by lazy {
        val trustAllCertificates = arrayOf<TrustManager>(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
            override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
        })

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, trustAllCertificates, java.security.SecureRandom())

        OkHttpClient.Builder()
            .cookieJar(JavaNetCookieJar(CookieManager()))
            .sslSocketFactory(sslContext.socketFactory, trustAllCertificates[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    val instance: RetrofitService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)) // GsonConverterFactory를 사용하여 JSON 응답을 변환합니다
            .build()

        retrofit.create(RetrofitService::class.java)
    }
}