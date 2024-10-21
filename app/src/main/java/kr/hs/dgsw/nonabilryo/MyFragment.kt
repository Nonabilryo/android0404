package kr.hs.dgsw.nonabilryo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFragment : Fragment() {

    private lateinit var retrofitService: RetrofitService
    private lateinit var profileImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my, container, false)

        sharedPreferencesManager = SharedPreferencesManager(requireContext())

        retrofitService = RetrofitClient.instance

        profileImageView = view.findViewById(R.id.profile)
        nameTextView = view.findViewById(R.id.tv_name)
        recyclerView = view.findViewById(R.id.recycler_view)

        // RecyclerView 설정
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // Adapter 설정 (Adapter 클래스는 필요에 따라 생성해 주세요)
        // recyclerView.adapter = YourAdapterClass()

        // 프로필 수정 클릭 리스너 설정
        val btnEdit: ImageButton = view.findViewById(R.id.btn_edit)
        btnEdit.setOnClickListener {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
        }

        fetchUserInfo("d950d16f-4197-4ac0-b27d-c164c46b4aa5")

        return view
    }

    private fun fetchUserInfo(userIdx: String) {
        val token: String? = sharedPreferencesManager.getAccessToken()
        retrofitService.getUserInfo(userIdx, token)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    try {
                        Log.d("UserResponse", "loginResponse: $response")
                        if (response.isSuccessful) {
                            val loginResponse = response.body()
                            Log.d("UserResponse", "loginResponse: $loginResponse")

                            // 서버로부터 온 원시 응답을 로그로 출력
                            val rawResponseString = response.errorBody()?.string()
                            Log.d("UserResponse", "Raw Response String: $rawResponseString")

                            loginResponse?.let { userResponse ->
                                Log.d("UserResponse", "Parsed Response Body: ${Gson().toJson(userResponse)}")
                                nameTextView.text = userResponse.data.name
                                userResponse.data.imageUrl?.let { imageUrl ->
                                    // Glide.with(this).load(imageUrl).into(profileImageView)
                                }
                            } ?: run {
                                Log.e("UserResponse", "Response body is null")
                            }
                        } else {
                            // 에러 응답을 로그로 출력
                            val errorResponse = response.errorBody()?.string()
                            Log.e("UserResponse", "Response Error: ${response.code()} - $errorResponse")
                        }
                    } catch (e: Exception) {
                        Log.e("UserResponse", "Parsing Error: ${e.localizedMessage}", e)
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.e("UserResponse", "Network Error: ${t.localizedMessage}", t)
                }
            })
    }
}