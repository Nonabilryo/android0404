package kr.hs.dgsw.nonabilryo

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

interface RetrofitService {
    @FormUrlEncoded
    @POST("sso/sign-up") // 회원가입 API 엔드포인트
    fun signup(
        @Field("name") name: String,
        @Field("id") id: String,
        @Field("password") password: String,
        @Field("email") email: String,
        @Field("phone") phone: String
    ): Call<SignupResponse>

    @FormUrlEncoded
    @POST("sso/login") // 로그인 API 엔드포인트
    fun login(
        @Field("userId") userId: String,
        @Field("password") password: String
    ): Call<LoginResponse>
}

object RetrofitClient {
    private const val BASE_URL = "https://10.80.161.224:8080/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    val instance: RetrofitService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // OkHttpClient 설정 추가
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(RetrofitService::class.java)
    }
}