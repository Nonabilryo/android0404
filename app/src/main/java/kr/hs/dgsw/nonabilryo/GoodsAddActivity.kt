package kr.hs.dgsw.nonabilryo

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class GoodsAddActivity : AppCompatActivity() {

    private lateinit var rentalTypeSpinner: Spinner
    private lateinit var cameraButton: ImageButton
    private lateinit var editName: EditText
    private lateinit var editCategory: EditText
    private lateinit var editDetail: EditText
    private lateinit var editPrice: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private val imagePaths = mutableListOf<String>()
    private val REQUEST_IMAGE_CAPTURE = 1
    private var currentPhotoPath: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods_add)

        // View 초기화
        rentalTypeSpinner = findViewById(R.id.spinner_rental_type)
        cameraButton = findViewById(R.id.btn_camera)
        editName = findViewById(R.id.edit_name)
        editCategory = findViewById(R.id.edit_category)
        editDetail = findViewById(R.id.edit_detail)
        editPrice = findViewById(R.id.edit_price)

        // SharedPreferences 초기화
        sharedPreferencesManager = SharedPreferencesManager(this)

        // RecyclerView 초기화
        recyclerView = findViewById(R.id.recycler_view)
        imageAdapter = ImageAdapter(imagePaths)
        recyclerView.adapter = imageAdapter

        // 이미지 가로 방향으로 스크롤
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        // 숫자 배열 생성
        val rentalTypes = arrayOf(0, 1, 2, 3)

        // ArrayAdapter를 사용하여 숫자를 Spinner에 설정
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, rentalTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        rentalTypeSpinner.adapter = adapter

        val backButton: ImageButton = findViewById(R.id.back_btn)
        backButton.setOnClickListener { finish() }

        // 카메라 버튼 클릭 이벤트
        cameraButton.setOnClickListener {
            checkCameraPermission()
            dispatchTakePictureIntent()
        }

        // 등록 버튼 클릭 이벤트
        val registerButton: Button = findViewById(R.id.button)
        registerButton.setOnClickListener {
            val rentalType = rentalTypeSpinner.selectedItem as Int

            // Retrofit POST 요청 보내기
            val token: String? = sharedPreferencesManager.getAccessToken()
            val titleBody = RequestBody.create("text/plain".toMediaTypeOrNull(), editName.text.toString())
            val categoryBody = RequestBody.create("text/plain".toMediaTypeOrNull(), editCategory.text.toString())
            val descriptionBody = RequestBody.create("text/plain".toMediaTypeOrNull(), editDetail.text.toString())
            val priceBody = RequestBody.create("text/plain".toMediaTypeOrNull(), editPrice.text.toString())
            val rentalTypeBody = RequestBody.create("text/plain".toMediaTypeOrNull(), rentalType.toString())

            // 이미지 목록 생성
            val imageParts = imagePaths.map { imagePath ->
                val file = File(imagePath)
                val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), file)
                MultipartBody.Part.createFormData("images", file.name, requestFile)
            }

            if (token != null) {
                RetrofitClient.instance.postArticle(
                    titleBody,
                    categoryBody,
                    descriptionBody,
                    priceBody,
                    rentalTypeBody,
                    imageParts,
                    token
                ).enqueue(object : Callback<ApiResponse> {
                    override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                        if (response.isSuccessful) {
                            Log.d("GoodsAddActivity", "Article posted successfully: ${response.body()?.message}")
                            Toast.makeText(this@GoodsAddActivity, "상품이 성공적으로 등록되었습니다.", Toast.LENGTH_SHORT).show()
                            navigateToHome()
                        } else {
                            Log.e("GoodsAddActivity", "Error posting article: ${response.code()} - ${response.message()}")
                            val errorResponse = response.errorBody()?.string()
                            Log.e("GoodsAddActivity", "Error body: $errorResponse")
                            Toast.makeText(this@GoodsAddActivity, "상품 등록에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                        Log.e("GoodsAddActivity", "Failed to post article: ${t.message}", t)
                        t.printStackTrace()
                        Toast.makeText(this@GoodsAddActivity, "네트워크 오류가 발생했습니다. 확인 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "인증 토큰이 유효하지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 카메라 권한 체크
    private fun checkCameraPermission() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), REQUEST_IMAGE_CAPTURE)
        }
    }

    // 권한 요청 결과 처리
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // 권한 허용됨
            } else {
                // 권한 거부됨
            }
        }
    }

    // 카메라 인텐트 호출
    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    // 카메라 촬영 결과 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            // 비트맵을 파일로 저장하고 그 경로를 currentPhotoPath에 저장
            currentPhotoPath = saveImage(imageBitmap)

            // 이미지 리스트에 추가하고 어댑터에 알림
            imagePaths.add(currentPhotoPath)
            imageAdapter.notifyItemInserted(imagePaths.size - 1)
        }
    }

    // 비트맵 이미지를 파일로 저장
    private fun saveImage(bitmap: Bitmap): String {
        val filename = "${System.currentTimeMillis()}.jpg"
        val file = File(externalCacheDir, filename)
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
        return file.absolutePath
    }

    // 홈 화면으로 이동
    private fun navigateToHome() {
        val intent = Intent(this, HomeFragment::class.java)
        startActivity(intent)
        finish() // 현재 액티비티 종료
    }
}