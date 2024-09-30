package kr.hs.dgsw.nonabilryo

import ImageAdapter
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GoodsAddActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_goods_add)

        // WindowInsets 설정
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // RecyclerView 설정
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val images = listOf(
            R.drawable.photho_rectangle,
            R.drawable.photho_rectangle,
            R.drawable.photho_rectangle,
            R.drawable.photho_rectangle,
            R.drawable.photho_rectangle
        )

        adapter = ImageAdapter(images)
        recyclerView.adapter = adapter

        // BackButton 설정
        val backButton: ImageButton = findViewById(R.id.back_btn)
        backButton.setOnClickListener {
            finish() // 현재 액티비티 종료
        }
    }
}