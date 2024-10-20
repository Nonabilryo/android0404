package kr.hs.dgsw.nonabilryo

import com.google.gson.GsonBuilder
import okhttp3.JavaNetCookieJar
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import java.net.CookieManager
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

interface RetrofitService {
    @POST("sso/sign-up")
    fun signUp(@Body signUpRequest: SignUpRequest): Call<SignupResponse>

    @POST("sso/verify/name")
    fun verifyName(@Body nameVerifyRequest: NameVerifyRequest): Call<SignupResponse>

    @POST("sso/verify/email")
    fun verifyEmail(@Body emailRequest: EmailRequest): Call<SignupResponse>

    @POST("sso/verify/emailCode")
    fun verifyEmailCode(@Body emailVerifyCode: EmailVerifyCodeRequest): Call<SignupResponse>

    @POST("sso/verify/tell")
    fun verifyTell(@Body tellRequest: TellRequest): Call<SignupResponse>

    @POST("sso/verify/tellCode")
    fun verifyTellCode(@Body tellVerifyCode: TellVerifyCodeRequest): Call<SignupResponse>

    @POST("sso/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

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
    fun getUserInfo(@Path("userIdx") userIdx: String, @Header("Authorization") auth: String?): Call<UserResponse>

    @Multipart
    @POST("/article")
    fun postArticle(
        @Part("title") title: RequestBody,
        @Part("category") category: RequestBody,
        @Part("description") description: RequestBody,
        @Part("price") price: RequestBody,
        @Part("rentalType") rentalType: RequestBody,
        @Part images: List<MultipartBody.Part>,
        @Header("Authorization") token: String?
    ): Call<ApiResponse>
}

object RetrofitClient {
    private const val BASE_URL = "http://52.79.55.88:8080/"

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
            .build()
    }

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    val instance: RetrofitService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        retrofit.create(RetrofitService::class.java)
    }
}